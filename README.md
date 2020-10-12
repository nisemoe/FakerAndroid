# FakerAndroid
A tool transfer a apk file to android project and support so hook include il2cpp c++ scaffolding
## 简介
直接将Apk文件转换为可以进行二次开发的Android项目的工具,支持so hook,对于il2cpp的游戏apk直接生成il2cpp c++脚手架
## 特点
- 基于AndroidStudio进行smali修改编译
- 提供Java层代码覆盖及继承替换的脚手架
- 提供so函数Hook Api
- 对于il2cpp的游戏apk直接生成il2cpp c++脚手架
- 无限的可能和延展性

### 运行环境
- IDEA
### 入口程序
- com.facker.toolchain.FakerMain
### 调用示例
- ```FakerTransfer.translate(apkFilePath);```
- AndroidStudio 项目生成路径为Apk平级目录
### 项目二次开发教程

##### 1、打开项目
      - Android studio直接打开工具生成的Android项目
      - 存在已知缺陷，res下的部分资源文件编译不过，需要手动修复，部分Manifest标签无法编译需要手动修复
##### 2、安装FakerAndroid-Idea插件
      - 插件地址https://github.com/Efaker/FakerAndroid-Idea/releases/tag/FakerAndroid-Idea0.0.1
      - 安装教程AndroidStudio File->Settings->plugins->setting->install from disk->重新启动AndroidStudio
##### 3、调试运行项目
      - AndroidStudio->build->FakeSmali->等待smali文件构建完成
      - Run 项目（提醒：DEX缓存原因，修改smali文件后调试运行需要先卸载手机调试包）
##### 4、进阶
      - smali 类继承
      - smali 类替换
      - so 函数 hook
      - il2cpp 脚手架的使用
##### 5、咨询探讨
      - QQ:1404774249
      - issues(在线解决) 
##### 6、给个star?免费的           
        
        
        
        
        
      
                
 








