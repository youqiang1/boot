# boot
springboot + dubbo

概述：
　　Dubbo是Alibaba开源的分布式服务框架，它最大的特点是按照分层的方式来架构，使用这种方式可以使各个层之间解耦合（或者最大限度地松耦合）。从服务模型的角度来看，Dubbo采用的是一种非常简单的模型，要么是提供方提供服务，要么是消费方消费服务，所以基于这一点可以抽象出服务提供方（Provider）和服务消费方（Consumer）两个角色。

Dubbo 的RPC 调用流程，这里主要涉及到4个模块：
    Registry：服务注册，我们一般会采取Zookeeper 作为我们的注册中心
    Provider：服务提供者（生产者），提供具体的服务实现
    Consumer：消费者，从注册中心中订阅服务
    Monitor：监控中心，RPC调用次数和调用时间监控
    
![Alt text](https://github.com/youqiang1/SpringCloudConfig/blob/master/static/picture/springboot-dubbo-1.png)

从上图中可以了解到整个RPC 服务调用的过程主要为：
    生产者发布服务到服务注册中心中
    消费者在服务注册中心中订阅服务
    消费者调用已经注册的服务

dubbo与springCloud对比

![Alt text](https://github.com/youqiang1/SpringCloudConfig/blob/master/static/picture/springboot-dubbo-2.png)
