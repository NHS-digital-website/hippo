package uk.nhs.digital.apispecs.swagger;

import io.swagger.codegen.v3.CodegenConstants;
import io.swagger.codegen.v3.CodegenOperation;
import io.swagger.codegen.v3.DefaultGenerator;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.*;

/**
 * There is requirement that all http methods of path should display 'Path parameters' and 'Headers' section.
 * This should have been handled by DefaultGenerator class of Codegen but seems it doesn't and that's the potential bug in Codegen code.
 *
 * This class CodegenDefaultGenerator extends DefaultGenerator class and fixes processOperation method.
 * It explicitly sets path parameters to operation parameters if operations.getParameters() is null
 *
 * &lt;pre&gt;
 * if (operation.getParameters() == null) {
 *                 List &lt;Parameter&gt; copy = new ArrayList<>(path.getParameters());
 *                 operation.setParameters(copy);
 *  }
 * &lt;pre/&gt;
 *
 * This class is a temporary construct and it should be removed once ADZ-439 and ADZ-440 are implemented.
 *
 */
public class CodegenDefaultGenerator extends DefaultGenerator {

    @Override
    public Map<String, List<CodegenOperation>> processPaths(Paths paths) {
        Map<String, List<CodegenOperation>> ops = new TreeMap<>();
        for (String resourcePath : paths.keySet()) {
            PathItem path = paths.get(resourcePath);
            processOperation(resourcePath, "get", path.getGet(), ops, path);
            processOperation(resourcePath, "head", path.getHead(), ops, path);
            processOperation(resourcePath, "put", path.getPut(), ops, path);
            processOperation(resourcePath, "post", path.getPost(), ops, path);
            processOperation(resourcePath, "delete", path.getDelete(), ops, path);
            processOperation(resourcePath, "patch", path.getPatch(), ops, path);
            processOperation(resourcePath, "options", path.getOptions(), ops, path);
        }
        return ops;
    }

    private void processOperation(String resourcePath, String httpMethod, Operation operation, Map<String, List<CodegenOperation>> operations, PathItem path) {
        if (operation == null) {
            return;
        }
        if (System.getProperty("debugOperations") != null) {
            LOGGER.info("processOperation: resourcePath= " + resourcePath + "\t;" + httpMethod + " " + operation + "\n");
        }
        List<Tag> tags = new ArrayList<>();

        List<String> tagNames = operation.getTags();
        List<Tag> swaggerTags = this.openAPI.getTags();
        if (tagNames != null) {
            if (swaggerTags == null) {
                for (String tagName : tagNames) {
                    tags.add(new Tag().name(tagName));
                }
            } else {
                for (String tagName : tagNames) {
                    boolean foundTag = false;
                    for (Tag tag : swaggerTags) {
                        if (tag.getName().equals(tagName)) {
                            tags.add(tag);
                            foundTag = true;
                            break;
                        }
                    }

                    if (!foundTag) {
                        tags.add(new Tag().name(tagName));
                    }
                }
            }
        }

        if (tags.isEmpty()) {
            tags.add(new Tag().name("default"));
        }

        /*
         build up a set of parameter "ids" defined at the operation level
         per the swagger 2.0 spec "A unique parameter is defined by a combination of a name and location"
          i'm assuming "location" == "in"
        */
        Set<String> operationParameters = new HashSet<>();
        if (operation.getParameters() != null) {
            for (Parameter parameter : operation.getParameters()) {
                operationParameters.add(generateParameterId(parameter));
            }
        }

        //need to propagate path level down to the operation
        if (path.getParameters() != null) {

            if (operation.getParameters() == null) {
                List<Parameter> copy = new ArrayList<>(path.getParameters());
                operation.setParameters(copy);

            } else {
                for (Parameter parameter : path.getParameters()) {
                    //skip propagation if a parameter with the same name is already defined at the operation level
                    if (!operationParameters.contains(generateParameterId(parameter))) {
                        operation.getParameters().add(parameter);
                    }
                }
            }
        }

        final Map<String, Schema> schemas = openAPI.getComponents() != null ? openAPI.getComponents().getSchemas() : null;
        final Map<String, SecurityScheme> securitySchemes = openAPI.getComponents() != null ? openAPI.getComponents().getSecuritySchemes() : null;
        final List<SecurityRequirement> globalSecurities = openAPI.getSecurity();
        for (Tag tag : tags) {
            try {
                CodegenOperation codegenOperation = config.fromOperation(resourcePath, httpMethod, operation, schemas, openAPI);
                codegenOperation.tags = new ArrayList<>(tags);
                config.addOperationToGroup(config.sanitizeTag(tag.getName()), resourcePath, operation, codegenOperation, operations);

                List<SecurityRequirement> securities = operation.getSecurity();
                if (securities != null && securities.isEmpty()) {
                    continue;
                }
                Map<String, SecurityScheme> authMethods = getAuthMethods(securities, securitySchemes);
                if (authMethods == null || authMethods.isEmpty()) {
                    authMethods = getAuthMethods(globalSecurities, securitySchemes);
                }

                if (authMethods != null && !authMethods.isEmpty()) {
                    codegenOperation.authMethods = config.fromSecurity(authMethods);
                    codegenOperation.getVendorExtensions().put(CodegenConstants.HAS_AUTH_METHODS_EXT_NAME, Boolean.TRUE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                String msg = "Could not process operation:\n" //
                    + "  Tag: " + tag + "\n"//
                    + "  Operation: " + operation.getOperationId() + "\n" //
                    + "  Resource: " + httpMethod + " " + resourcePath + "\n"//
                    // + "  Definitions: " + swagger.getDefinitions() + "\n"  //
                    + "  Exception: " + ex.getMessage();
                throw new RuntimeException(msg, ex);
            }
        }

    }

    private static String generateParameterId(Parameter parameter) {
        return parameter.getName() + ":" + parameter.getIn();
    }

    private Map<String, SecurityScheme> getAuthMethods(List<SecurityRequirement> securities, Map<String, SecurityScheme> securitySchemes) {
        if (securities == null || securitySchemes == null || securitySchemes.isEmpty()) {
            return null;
        }
        final Map<String, SecurityScheme> authMethods = new HashMap<>();
        for (SecurityRequirement requirement : securities) {
            for (String key : requirement.keySet()) {
                SecurityScheme securityScheme = securitySchemes.get(key);
                if (securityScheme != null) {
                    authMethods.put(key, securityScheme);
                }
            }
        }
        return authMethods;
    }
}
