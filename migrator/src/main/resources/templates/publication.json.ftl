<#-- @ftlvariable name="publication" type="uk.nhs.digital.ps.migrator.model.hippo.Publication" -->
{
  "name" : "${publication.jcrNodeName}",
  "primaryType" : "publicationsystem:publication",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${publication.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${publication.localizedName}" ]
  }, {
    "name" : "publicationsystem:Title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${publication.title}" ]
  }, {
    "name" : "publicationsystem:InformationType",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "Experimental statistics" ]
  }, {
    "name" : "publicationsystem:Summary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "Publication" ]
  }, {
    "name" : "publicationsystem:CoverageEnd",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "0001-01-01T12:00:00.000Z" ]
  }, {
    "name" : "publicationsystem:NominalDate",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "0001-01-01T12:00:00.000Z" ]
  }, {
    "name" : "publicationsystem:KeyFacts",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "" ]
  }, {
    "name" : "publicationsystem:CoverageStart",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "0001-01-01T12:00:00.000Z" ]
  }, {
    "name" : "publicationsystem:PubliclyAccessible",
    "type" : "BOOLEAN",
    "multiple" : false,
    "values" : [ "true" ]
  }, {
    "name" : "publicationsystem:AdministrativeSources",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "" ]
  }, {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  } ],
  "nodes" : [ ]
}
