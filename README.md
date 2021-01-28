### 线程池
#### 线程池构造函数的参数
- corePoolSize：int，核心线程数-线程池初始化后默认没有任何线程，等待任务来时创建新的线程去执行任务
- maxPoolSize：int，最大线程数-在核心线程数基础上额外增加一些线程，但新增加的线程数也有一个上限
- keepAliveTime：long，保持存活时间
- workQueue：BlockingQueue，任务存储队列
- threadFactory：ThreadFactory，当前成需要新的线程的时候，会用threadFactory来生成新的线程
- Handler：RejectExecutionHandler，由于线程池无法接受你所提交的任务的拒绝策略
#### 线程池线程添加规则
- 如果线程数小于corePoolSize，即使其他工作线程处于空闲状态，也会创建一个新线程来运行新任务
- 如果线程数等于(或大于)corePoolSize但小于maxPoolSize，则将任务放入队列
- 如果队列已满，并且线程数小于maxPoolSize，则创建一个新的线程来运行任务
- 如果队列已满，并且线程数大于或等于maxPoolSize，则拒绝该任务
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E6%B7%BB%E5%8A%A0%E7%BA%BF%E7%A8%8B%E8%A7%84%E5%88%99%20%20.png" width = "600" height = "300" alt="主内存和本地内存的图示2" align=center /><br/>
