# What if I want to delete database

If you want to drop all tables, you can do it with these simple commands.

This will of course delete all configuration and all content.

```
sudo grep "password=" /home/hippo_authoring/tomcat/conf/context.xml
set H_PASSWORD="..."
mysqldump -u hippocms -p${H_PASSWORD} -h hippo-authoring.mysql.db.int --no-data hippocms \
  | grep "DROP TABLE" \
  | mysql -u hippocms -p${H_PASSWORD} -h hippo-authoring.mysql.db.int hippocms
```
