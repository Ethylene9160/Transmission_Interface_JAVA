# Transmission_Interface_JAVA

## 一个基于TCP传输协议的网络信息收发接口

使用静态代理，实现不同进程间的通信。在程序中，建立TCP通讯套接字：

<p align = 'center'>
Socket={< IP >:< Port >}
</p>

声明需要对接的进程的IP地址和进程信息，建立套接字连接。其中，Socket的声明可以是：
```java
int HOST_PORT = 9998;
Socket socket = new Socket("host_ip", HOST_PORT);
```
例如：
```java
Socket socket = new Socket("127.0.0.1", 9998);
```

## 静态代理的实现

Key: `TransmissionController`, `TransmissionListener`

* 静态代理类TransmissionController
  
  收取和发送信息，都由它进行完成。查看该代理类提供一个有关发送消息的成员方法

