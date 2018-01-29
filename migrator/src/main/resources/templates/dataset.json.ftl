<#-- @ftlvariable name="dataset" type="uk.nhs.digital.ps.migrator.model.hippo.DataSet" -->
{
  "name" : "${dataset.jcrNodeName}",
  "primaryType" : "publicationsystem:dataset",
  "mixinTypes" : [ "mix:referenceable", "hippotaxonomy:classifiable" ],
  "properties" : [ {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.localizedName}" ]
  }, {
    "name" : "publicationsystem:Title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.title}" ]
  }, {
    "name" : "publicationsystem:Granularity",
    "type" : "STRING",
    "multiple" : true,
    "values" : [ "${dataset.granularity}" ]
  }, {
    "name" : "publicationsystem:CoverageEnd",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "${dataset.coverageEnd}" ]
  }, {
    "name" : "publicationsystem:NominalDate",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "${dataset.nominalDate}" ]
  }, {
    "name" : "publicationsystem:NextPublicationDate",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "${dataset.nextPublicationDate}" ]
  }, {
    "name" : "publicationsystem:CoverageStart",
    "type" : "DATE",
    "multiple" : false,
    "values" : [ "${dataset.coverageStart}" ]
  },<#if dataset.taxonomyKeys?has_content> {
       "name" : "hippotaxonomy:keys",
       "type" : "STRING",
       "multiple" : true,
       "values" : [ "${dataset.taxonomyKeys}" ]
  },</#if> {
    "name" : "publicationsystem:Summary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.summary}" ]
  }, {
    "name" : "publicationsystem:GeographicCoverage",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${dataset.geographicCoverage}" ]
  }, {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  } ],
  "nodes" : [
  <#list dataset.attachments as attachment>
  {
    "name":"publicationsystem:Files-v3",
    "primaryType" : "publicationsystem:attachment",
    "mixinTypes":[],
    "properties" : [ {
        "name" : "publicationsystem:displayName",
        "type" : "STRING",
        "multiple" : false,
        "values" : [ "${attachment.title}" ]
      } ],
    "nodes" : [ {
      "name" : "publicationsystem:attachmentResource",
      "primaryType":"publicationsystem:resource",
      "mixinTypes":[],
      "properties":[ {
          "name":"hippo:filename",
          "type":"STRING",
          "multiple":false,
          "values":[ "${attachment.fileName}" ]
        }, {
          "name":"jcr:data",
          "type":"BINARY",
          "multiple":false,
          "values":[ "file://${attachment.filePath}" ]
        }, {
          "name":"jcr:mimeType",
          "type":"STRING",
          "multiple":false,
          "values":[ "${attachment.mimeType}" ]
        }, {
          "name":"jcr:lastModified",
          "type":"DATE",
          "multiple":false,
          "values":[ "0001-01-01T12:00:00.000Z" ]
        } ],
      "nodes":[]
    }]
  } <#sep>,</#sep>
  </#list>
  <#if dataset.attachments?size != 0 && dataset.resourceLinks?size != 0>,</#if>
  <#list dataset.resourceLinks as resourceLink>
  {
    "name" : "publicationsystem:ResourceLinks",
    "primaryType" : "publicationsystem:relatedlink",
    "mixinTypes" : [ ],
    "properties" : [ {
      "name" : "publicationsystem:linkText",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${resourceLink.name}" ]
    }, {
      "name" : "publicationsystem:linkUrl",
      "type" : "STRING",
      "multiple" : false,
      "values" : [ "${resourceLink.uri}" ]
    } ],
    "nodes" : [ ]
  } <#sep>,</#sep>
  </#list>
  ]
}
