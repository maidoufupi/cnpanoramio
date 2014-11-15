## Demos
qq map for angular ui demo and documents: 
[http://anypossiblew.github.io/ui-map-qq/](http://anypossiblew.github.io/ui-map-qq/)

## Get started
1. 假设系统已经安装有npm（如果没有，请安装nodejs带有npm）
2. 如果没有安装grunt-cli则使用如下命令安装  
```npm install -g grunt-cli```  
3. 如果没有安装bower则使用如下命令安装（Bower requires Node and npm and Git.）  
```npm install -g bower```  
4. Change to the project's root directory.  
5. Install project dependencies with ```npm install```  
6. Install bower dependencies with ```bower install```  
7. Run Grunt with ```grunt serve```  

如果运行时出现  

TypeError: Cannot read property 'stdout' of undefined  
at compile (..\node_modules\grunt-contrib-compass\tasks\compass.js:37:10)  
          ..
        at Object.oncomplete (fs.js:107:15) Use --force to continue.  
    
则需要安装[ruby](https://www.ruby-lang.org/en/downloads/)其中带有compass组件  

### CoffeeScript
本项目使用了CoffeeScript，所以为了能编译请确保安装npm coffee-script插件，use 
```
npm install -g coffee-script
```

