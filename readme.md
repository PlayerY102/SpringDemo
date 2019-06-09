请在./src/resources/application.prop中正确设置好数据库用户和密码
并建立相关数据库。
注意设置中create和none模式的区别。
create会建立并初始化所有需要的表，none模式下会使用以前的表和数据。

这是有区块链验证的版本，需要将hello.Fabric.HFJavaExample中的所有
http://119.3.211.100:7054替换成你自己建立的区块链的ca节点的地址。

运行命令：mvn spring-boot:run
也可以打包 mvn package
然后运行 target目录下的jar包
java -jar ***.jar