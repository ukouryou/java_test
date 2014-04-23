[1mdiff --git a/pom.xml b/pom.xml[m
[1mindex 1bca6ec..c833fd0 100644[m
[1m--- a/pom.xml[m
[1m+++ b/pom.xml[m
[36m@@ -21,6 +21,16 @@[m
       <version>4.7</version>[m
       <scope>test</scope>[m
     </dependency>[m
[32m+[m	[32m<dependency>[m
[32m+[m	[32m    <groupId>org.apache.commons</groupId>[m
[32m+[m	[32m    <artifactId>commons-lang3</artifactId>[m
[32m+[m	[32m    <version>3.3</version>[m
[32m+[m	[32m</dependency>[m
[32m+[m	[32m<dependency>[m
[32m+[m	[32m    <groupId>org.apache.commons</groupId>[m
[32m+[m	[32m    <artifactId>commons-collections4</artifactId>[m
[32m+[m	[32m    <version>4.0</version>[m
[32m+[m	[32m</dependency>[m
 [m
     <dependency>[m
       <groupId>jakarta-regexp</groupId>[m
[36m@@ -84,28 +94,12 @@[m
     <artifactId>spymemcached</artifactId>[m
     <version>2.10.5</version>[m
 </dependency>[m
[31m-    <dependency>[m
[31m-      <groupId>tianji</groupId>[m
[31m-      <artifactId>network4-api</artifactId>[m
[31m-      <version>1.18.8</version>[m
[31m-      <scope>provided</scope>[m
[31m-    </dependency>[m
 <dependency>[m
     <groupId>redis.clients</groupId>[m
     <artifactId>jedis</artifactId>[m
     <version>2.4.0</version>[m
 </dependency>[m
[31m-    <dependency>[m
[31m-      <groupId>tianji</groupId>[m
[31m-      <artifactId>network4-rest-agent</artifactId>[m
[31m-      <version>1.18.8</version>[m
[31m-      <scope>provided</scope>[m
[31m-    </dependency>[m
[31m-  <dependency>[m
[31m-        <groupId>com.tianji</groupId>[m
[31m-        <artifactId>tianji-rest-java</artifactId>[m
[31m-        <version>1.2-SNAPSHOT</version>[m
[31m-      </dependency>[m
[32m+[m
       <dependency>[m
     <groupId>net.sf.ehcache</groupId>[m
     <artifactId>ehcache</artifactId>[m
