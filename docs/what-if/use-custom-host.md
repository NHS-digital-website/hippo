# What If I want to use custom host

create new file at
`repository-data/development/src/main/resources/hcm-config/hst/hosts-allowed.yaml`.
This file is ignored by Git.

This is example content to add custom `127.0.0.2` IP with CMS on `127.0.0.2:4040`
config.

```YAML
---
definitions:
  config:
    /hst:hst/hst:hosts/localhost-two:
      /127.0.0.2:
        /hst:root:
          hst:contextpath: /site
          hst:homepage: root
          hst:mountpoint: /hst:hst/hst:sites/common
          jcr:primaryType: hst:mount
        hst:scheme: http
        jcr:primaryType: hst:virtualhost
      hst:cmslocation: http://127.0.0.2:4040/cms
      hst:defaultport: 8080
      jcr:primaryType: hst:virtualhostgroup
```
