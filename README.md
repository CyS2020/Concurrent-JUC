### 线程池
#### 线程池构造函数的参数
- corePoolSize：int，核心线程数--线程池初始化后默认没有任何线程，等待任务来时创建新的线程去执行任务
- maxPoolSize：int，最大线程数--在核心线程数基础上额外增加一些线程，但新增加的线程数也有一个上限
- keepAliveTime：long，保持存活时间--线程数多于corePoolSize，多于的线程空闲时间超过keepAliveTime就会被终止
- workQueue：BlockingQueue，任务存储队列
- threadFactory：ThreadFactory，当前成需要新的线程的时候，会用threadFactory来生成新的线程--默认Executors.defaultThreadFactory()
- Handler：RejectExecutionHandler，由于线程池无法接受你所提交的任务的拒绝策略
#### 线程池线程添加规则
- 如果线程数小于corePoolSize，即使其他工作线程处于空闲状态，也会创建一个新线程来运行新任务
- 如果线程数等于(或大于)corePoolSize但小于maxPoolSize，则将任务放入队列
- 如果队列已满，并且线程数小于maxPoolSize，则创建一个新的线程来运行任务
- 如果队列已满，并且线程数大于或等于maxPoolSize，则拒绝该任务
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E6%B7%BB%E5%8A%A0%E7%BA%BF%E7%A8%8B%E8%A7%84%E5%88%99%20%20.png" width = "600" height = "250" alt="主内存和本地内存的图示2" align=center /><br/>
#### 常见队列类型
- 直接交接：SynchronousQueue
- 无界队列：LinkedBlockingQueue
- 有界队列：ArrayBlockingQueue
#### 默认线程池
- newFixedThreadPool：由于传入的LinkedBlockingQueue是没有容量上限的，所以当请求数量越来越多无法及时处理，占用大量内存造成OOM
- newSingleThreadPool：和上方原理一样，只不过把线程数直接设置为1，所以存在同样的问题，请求堆积时占用大量内存会发生OOM
- newCachedThreadPool：弊端在于第二个参数最大线程数设置为Integer.MAX_VALUE，这可能回导致创建非常多的线程导致OOM，使用直接交接队列
- newScheduledThreadPool：支持定时及周期性任务执行的线程池，最大线程数为Integer.MAX_VALUE，使用DelayedWorkQueue队列
#### 线程池线程数设置多少合适？
- CPU密集型(加密、计算hash等)：最佳线程数为CPU核心数的1-2倍左右
- 耗时IO型(读写数据库、文件、网络读写等)：最佳线程数一般会大于cpu核心数的很多倍，压测为准
- 线程数 = CPU核心数 * (1 + 平均等待时间/平均工作时间)
