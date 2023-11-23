# Transmission_Interface_JAVA
![Static JDK](https://img.shields.io/badge/JDK-18-green)
![Static Version](https://img.shields.io/badge/Version-beta1.0-blue)
![Static License](https://img.shields.io/badge/License-MIT-orange)
![Static Date](https://img.shields.io/badge/Date-2023--11--23-lightgrey)

## 项目结构

```bash
Transmission_Interface_JAVA
├─.idea
│  └─inspectionProfiles
├─doc
│  └─doc.zip
├─out
│  └─production
│      └─Webcontact_Interface_JAVA
│          └─web_tools
├─src
│   ├─web_tools
│   │   ├─TransmissionController.java
│   │   ├─TransmissionListener.java
│   │   ├─WebUtil.java
│   │   └─MyData.java
│   ├─Main.java
│   └─MyServer.java
└─README.md 
```
## 基于TCP传输协议的网络信息收发接口

使用静态代理，实现不同进程间的通信。在程序中，需要建立TCP通讯套接字：

$$
Socket:={ <IP address>,~<Port>}
$$

客户机声明需要对接的进程的IP地址和进程信息，建立套接字连接。其中，Socket的声明可以是：
```java
int HOST_PORT = 9998;
Socket socket = new Socket("host_ip", HOST_PORT);
```
例如：
```java
Socket socket = new Socket("127.0.0.1", 9998);
```

服务机只需要声明自己的进程信息即可，在`ServerSocket`中有对服务机地址的封装。例如：
```java
int SERVER_PORT = 9998;
ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
```

## 客户机和服务机套接字的连接：

假设服务机：
```java
class MyServer{
    public static void main(String[] args){
        int SERVER_PORT = 9998;
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        Socket socket = serverSocket.accept();//等待服务机的连接
    }
};
```

客户机：

```java
class MyClient{
    public static void main(String[] args){
        int HOST_PORT = 9998;
        //一旦这一行被使用，客户机就会向服务机发送连接请求。
        String HOST_IP = "127.0.0.1";//本机连接（）
        Socket socket = new Socket(HOST_IP, HOST_PORT);
    }
};
```

这样就能实现客户机和主机的通信了。

将不同的socket保存，就能实现一个服务机与多个被服务进程的连接。

```java
class MyServer{
    List<Socket> clients;
    public static void main(String[] args){
        clients = new ArrayList<Socket>();
        int SERVER_PORT = 9998;
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        while(true){
            Socket socket = serverSocket.accept();//等待服务机的连接
            clients.add(socket);//向客户机列表中添加刚刚连接的客户机
        }
    }
};
```

如果实现服务机与客户机各自的通信，需要开启多线程，每个线程负责一个客户机的通信。

```java
class MyServer{
    List<Socket> clients;
    public static void main(String[] args){
        clients = new ArrayList<Socket>();
        int SERVER_PORT = 9998;
        ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
        while(true){
            Socket socket = serverSocket.accept();//等待服务机的连接
            clients.add(socket);//向客户机列表中添加刚刚连接的客户机
            new Thread(new Runnable(){
                @Override
                public void run(){
                    //在这里实现服务机与客户机的通信内容。
                }
            }).start();
        }
    }
};
```

请参考例程`Main.java`和`MyServer.java`。

## 特性

使用*静态代理*实现不同进程间的通信。

Key: `TransmissionController`, `TransmissionListener`

* 静态代理类TransmissionController
  
  收取和发送信息，都由它进行完成。查看该代理类提供一个有关发送消息的成员方法.
  
  ```java
    public void send(Object message)
    ```
  
    该方法的参数是一个`Object`对象，发送的对象**必须实现`Serializable`接口**。
  > **为什么需要实现`Serializable`接口？**
  > 
  > 为了将程序中的对象转换为字节流，以便于在网络中传输。
  > `Serializable`接口是一个标记接口，没有任何方法，只是用来标记一个类的对象可以被序列化。
  > 被标记为序列化的对象被默认转换为字节流，以便于在网络中传输；
  > 传输结束后，通过反序列化，可以将字节流转换为对象。
  > 
  > 需要注意的是，如果一个类的对象需要被序列化，那么这个类的**所有成员变量都需要实现`Serializable`接口**。
  > 
  > 当然，如果不希望某个成员变量被序列化，可以使用`transient`关键字修饰该成员变量。
  > 
  > 此外，如果希望用别的方法进行序列化，可以实现`java.io.Externalizable`接口，该接口继承自`Serializable`接口，
  > 对于`Externalizable`接口的实现类，需要实现两个方法：
  > 
  > ```java
  > //序列化
  > public void writeExternal(ObjectOutput out) throws IOException;
  > //反序列化
  > public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException;
  > ```
  > 这样也能实现序列化和反序列化，只是不再使用java默认的序列化方法。
  > 
  > 当然，如果功力十足，直接发送二进制流然后在接收端进行解码也是很酷的的选择。
  > 
  >**为什么需要在网络中传输字节流？**
  > 
  > 为了在不同进程间传输信息。

## 使用方法

1. 在需要使用的进程中，建立TCP通讯套接字，声明需要对接的进程的IP地址和进程信息，建立套接字连接。其中，Socket的声明可以是：
```java
int HOST_PORT = 9998;
Socket socket = new Socket("host_ip", HOST_PORT);
```
2. 在需要使用的进程中，建立静态代理类TransmissionController的实例，传入套接字socket，并传入代理对象`TransmissionListener`的实现类建立代理类与对应进程的连接。
```java
TransmissionController transmissionController = new TransmissionController(socket, new TransmissionListener<String>() {
    @Override
    public void onTransmissionStart(){
        system.out.println("Transmission Start!");
    }
    
    @Override
    public void onTransmissionSuccess(Object o){
        //接收到消息时，会将这个参数传递进来。
        //在这里处理接收到的消息。
        system.out.println("Transmission Success! received object: " + o.toString());
    }
    
    @Override
    public void onTransmissionEnd(){
        system.out.println("Transmission End!");
    }
    
    @Override
    public void onTransmissionProgress(String message, ErrorType errorType){
        system.out.println("Transmission Error! message: " + message + ", errorType: " + errorType.toString());
    }
    
    @Override
    public void alertError(String error){
        system.out.println("Transmission Failure! Exception: " + error);
    }
});
```
3. 在需要使用的进程中，使用代理类的成员方法`send(T t)`发送消息。需要注意的是，发送的对象**必须实现`Serializable`接口**。
```java
transmissionController.send("Hello World!");
```

例程`Main.java`和`MyServer.java`中，有对这个接口的使用。

如果只希望发送字符串的话，作者的另一个仓库[useless_web_interface](https://github.com/Ethylene9160/useless_web_interface)中有一个更简单的实现。


