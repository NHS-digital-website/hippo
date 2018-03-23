# What if you want to test hosts setup

Edit /etc/hosts file and add the following lines to it.

```
127.0.0.1  beta.digital.nhs.uk
127.0.0.1   www.digital.nhs.uk
127.0.0.1       digital.nhs.uk
127.0.0.1      cms-dev.nhsd.io
127.0.0.1          dev.nhsd.io
127.0.0.1      cms-uat.nhsd.io
127.0.0.1          uat.nhsd.io
127.0.0.1      cms-tst.nhsd.io
127.0.0.1          tst.nhsd.io
127.0.0.1 cms-training.nhsd.io
127.0.0.1     training.nhsd.io
```

Now after starting application you should be able to access the following sites:

* http://beta.digital.nhs.uk:8080
* http://www.digital.nhs.uk:8080
* http://digital.nhs.uk:8080
* http://dev.nhsd.io:8080
* http://training.nhsd.io:8080
* http://tst.nhsd.io:8080
* http://uat.nhsd.io:8080
