# What If I want to add test content

- Change `application` to `development` in the [autoexport-module.yaml](../../repository-data/application/src/main/resources/hcm-config/configuration/modules/autoexport-module.yaml) file:

```YAML
---
definitions:
  config:
    /hippo:configuration/hippo:modules/autoexport:
      /hippo:moduleconfig:
        autoexport:modules:
        - repository-data/development:/
```

- After starting up the application login to the CMS, and **switch Autoexport on**
- Add new documents, make changes to existing documents as you wish
- Only track and commit the file changes that happened under the `development` folder
- You're done :)