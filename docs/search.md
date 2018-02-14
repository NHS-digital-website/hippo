# Search

Currently the plan is to have one search across whole digital website.




## Facets

Facet headers are defined in `documents/administration/facet-headers.yaml`




### Document Type

Document type facet is driven by `common:FacetType` field that can be set in
document type prototype

```yaml
---
definitions:
  config:
    /hippo:namespaces/publicationsystem/publication:
      /editor:templates:
        ...
      /hipposysedit:nodetype:
        ...
      /hipposysedit:prototypes:
        /hipposysedit:prototype:
          common:FacetType: publication
```

You also have to remember to create `labels.publication` resource bundle item and
include it in `.../freemarker/common/facets.ftl` template.
