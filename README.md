
# FakerAndroid
A tool translate apk file to common android project and support so hook and include il2cpp c++ scaffolding when apk is a il2cpp game apk
## 简介
- 直接将Apk文件转换为可以进行二次开发的Android项目的工具,支持so hook,对于il2cpp的游戏apk直接生成il2cpp c++脚手架
- 将痛苦的逆向环境，转化为舒服的开发环境，告别汇编，告别二进制，还有啥好说的~~！
## 特点

- 基于AndroidStudio进行smali修改编译
- 提供Java层代码覆盖及继承替换的脚手架
- 提供so函数Hook Api
- 对于il2cpp的游戏apk直接生成il2cpp c++脚手架
- 无限的可能性和扩展性,能干啥你说了算~

### 运行环境
- IntelliJ IDEA
### 入口程序
- com.facker.toolchain.FakerMain
### 使用方式
- ```FakerTransfer.translate(apkFilePath);```
- AndroidStudio 项目生成路径为Apk平级目录
### 生成的Android项目二次开发教程

##### 1、打开项目
- Android studio直接打开工具生成的Android项目
- 存在已知缺陷，res下的部分资源文件编译不过，需要手动修复，部分Manifest标签无法编译需要手动修复
##### 2、AndroidStudio安装FakerAndroid-Idea插件
- [下载地址](https://github.com/Efaker/FakerAndroid-Idea/releases/tag/FakerAndroid-Idea0.0.1)
- 安装教程AndroidStudio File->Settings->plugins->setting->install from disk->重新启动AndroidStudio
##### 3、调试运行项目
- AndroidStudio->build->FakeSmali->等待smali文件构建完成
- Run 项目（提醒：DEX缓存原因，修改smali文件后调试运行需要先卸载手机调试包）
##### 4、进阶
- 借助javaScaffoding 使用java代码对smali 类继承  
  在javaScaffoding模块创建伪类,保持伪类类签名与要继承的类的smali文件路径名称对应->AndroidStudio右侧 Gradle->javaScaffoding->build->build,在主模块（app/src/main/java）中编写自己的类对伪类进行继承  
- 借助FakeSmali 使用java代码对smali 类替换  
  在主模块（app/src/main/java）编写自己的类，类签名与要替换的类的smali文件路径名称对应
- 借助FakeCpp 使用jni对so函数进行hook替换
        

- 借助il2cpp Scaffolding 和FakeCpp,使用jni对il2cpp游戏脚本进行二次开发


##### 5、遇到问题了？兄弟别走肯定能用，而且是你最
佳的解决方案，咨询探讨
- QQ:1404774249
- issues(在线解决) 
##### 6、给个star?免费的           
        
        
        
        
        
      
                
 








