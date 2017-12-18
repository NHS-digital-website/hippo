<#-- @ftlvariable name="series" type="uk.nhs.digital.ps.migrator.model.hippo.Series" -->
{
  "name" : "${series.jcrNodeName}",
  "primaryType" : "publicationsystem:series",
  "mixinTypes" : [ "mix:referenceable" ],
  "properties" : [ {
    "name" : "hippotranslation:locale",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "en" ]
  }, {
    "name" : "publicationsystem:Title",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${series.title}" ]
  }, {
    "name" : "publicationsystem:Summary",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${series.summary}" ]
  }, {
    "name" : "jcr:path",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${series.jcrPath}" ]
  }, {
    "name" : "jcr:localizedName",
    "type" : "STRING",
    "multiple" : false,
    "values" : [ "${series.localizedName}" ]
  } ],
  "nodes" : [ ]
}
