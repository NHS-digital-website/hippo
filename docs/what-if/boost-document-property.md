# What if I Want to Boost Document Proper

With Jackrabbit it's possible to "boost" nodes and node properties so that it get
higher score in search results.

More information about indexing configuration is
[available here](IndexingConfiguration).




## Jackrabbit Index Configuration

First make sure that your `indexingConfiguration` xml is using 1.1 DTD. The `regexp`
matcher was is not valid in version 1.0.

```
<!DOCTYPE configuration SYSTEM "http://jackrabbit.apache.org/dtd/indexing-configuration-1.1.dtd">
```

When creating indexing rule, make sure to include all other fields in the index,
Hippo CMS is using it under the bonnet. You can however wish to exclude it from
node scope (`.`).

```
<index-rule
  nodeType="publicationsystem:publication"
  boost="2.0"
>
  <property boost="10.0">publicationsystem:Title</property>
  <property boost="3.0">publicationsystem:Summary</property>
  <property boost="1.0">publicationsystem:KeyFacts</property>
  <property boost="1.0" isRegexp="true" nodeScopeIndex="false">.*:.*</property>
</index-rule>
```

At this point I still don't know how to get score (boost) set on individual fields
to be used when searching entire NodeScope.

That's why in your search component you have to explicitly search for the query in
the selected fields and NodeScope as well

* `publicationsystem:Title`
* `publicationsystem:Summary`
* `publicationsystem:KeyFacts`
* `.`




[IndexingConfiguration]: https://wiki.apache.org/jackrabbit/IndexingConfiguration
