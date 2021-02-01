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
- workStealingPool：是jdk1.8加入的，线程中的任务有子任务的时候，拥有窃取能力--窃取子任务队列中的任务
#### 线程池线程数设置多少合适？
- CPU密集型(加密、计算hash等)：最佳线程数为CPU核心数的1-2倍左右
- 耗时IO型(读写数据库、文件、网络读写等)：最佳线程数一般会大于cpu核心数的很多倍，以压测为准
- 线程数 = CPU核心数 * (1 + 平均等待时间/平均工作时间)
#### 停止线程池的正确方法
- shutdown：阻止新来的任务提交，对已经提交了的任务不会产生任何影响
- isShutdown：当调用shutdown()或shutdownNow()方法后返回为true
- isTerminated：isTerminated当调用shutdown()方法后，并且所有提交的任务完成后返回为true
- awaitTermination：当等待超过设定时间时，会监测ExecutorService是否已经关闭，若关闭则返回true，否则返回false
- shutdownNow：阻止新来的任务提交，同时会中断当前正在运行的线程，即workers中的线程，返回workQueue中的任务
#### 线程池拒绝任务时机
- 当Executor关闭时，提交新任务会被拒绝
- 以及当Executor对最大线程和工作队列容量使用有限边界并且已经饱和时
#### 四种拒绝策略
- AbortPolicy：抛出异常
- DiscardPolicy：默默丢弃
- DiscardOldestPolicy：丢弃最老的任务
- CallerRunsPolicy：提交任务的线程负责执行
#### 钩子方法
- 暂停线程池、每个任务执行前后、日志与统计
#### 线程池组成部分
- 线程池管理器
- 工作线程
- 任务队列
- 任务接口(Task)
- Executor -> ExecutorService -> AbstractExecutorService -> ThreadPoolExecutor
#### 线程池状态
- RUNNING：接收新任务并处理排队任务
- SHUTDOWN：不接受新任务，但处理排队任务
- STOP：不接受新任务，也不处理排队任务，中断正在进行的任务
- TIDYING：所有任务都已终止，workerCount为零时线程会切换到该状态，并允许terminate()钩子方法
- TERMINATED：terminate()运行完成
### ThreadLocal
#### 使用场景
- 每个线程需要一个独享的对象(通常是工具类，典型需要使用的类有SimpleDateFormat和Random)--重写initialValue()
  - 在ThreadLocal第一次get的时候把对象给初始化出来，对象的初始化时机由我们控制
  - initialValue()：该方法返回当前线程对应的"初始值"，这是一个延迟加载的方法，调用get()的时候，才会触发
  - 当线程第一次使用get方法访问变量时，将调用此方法，除非线程先前调用了set方法，这样就不会为线程调用initialValue方法
  - 通常每个线程最多调用一次此方法，但如果调用了remove()后，再调用get()，则可以再次调用此方法
- 每个线程内需要保存全局变量(例如在拦截器中获取用户信息)，可以让不同的方法直接使用，避免参数传递的麻烦--调用set()
  - ThreadLocal里的对象的生成时机不由我们随意控制，用ThreadLocal.set()直接放到ThreadLocal中去以便后续使用
#### 两个作用
- 让某个需要用到的对象在线程间隔离(每个线程都有自己独立的对象)
- 在任何方法中都可以轻松获取到该对象，调用ThreadLocal.get()方法
#### ThreadLocal带来的好处
- 达到线程安全
- 不需要加锁，提高执行效率
- 高效利用内存、节省开销
- 免去传参的繁琐
#### ThreadLocal基本原理
- ThreadLocalMap保存在Thread中的，即ThreadLocal与其保存的数据均存放在线程中
- ThreadLocalMap解决hash冲突的方式是：线性探测法
- get方法先是取出当前线程的ThreadLocalMap，然后调用map.getEntry方法，把本ThreadLocal的引用即this作为参数传入，取出map中属于本ThreadLocal的value<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/ThreadLocal%E5%8E%9F%E7%90%86%E5%9B%BE.png" width = "600" height = "300" alt="主内存和本地内存的图示2" align=center /><br/>
#### ThreadLocal主要方法
- T initialValue()：初始化
- void set(T t)：为线程设置一个新值
- T get()：得到这个线程对应的value，如果首次调用则调用initialValue来得到这个值
- void remove()：删除对应这个线程的值
#### ThreadLocal注意点
- 内存泄漏
  - 调用链：Thread -> ThreadLocalMap -> Entry(key为null) -> Value
- 空指针异常
  - 在get之前如果不进行初始化，会返回null，注意基本数据类型的装箱拆箱导致空指针异常
- 共享对象
  - 如果每个线程中ThreadLocal.set()的数据是多线程共享的同一个对象，那么多个线程的ThreadLocal.get()取得的数据还是共享对象本身，还是有并发访问的问题，例如不应该在ThreadLocal中放入静态的对象
- 如果可以不使用ThreadLocal就能解决问题，就不要强行使用ThreadLocal
- 优先使用框架的支持，而不是自己创造
  - 在Spring中，如果可以使用RequsetContextHolder，那么就不需要自己维护ThreadLocal
- 每次http请求都对应一个线程，线程之间互相隔离，这就是ThreadLocal的典型应用场景
### 锁(Lock)
#### Lock接口方法
- lock()：就是最普通的锁如果锁已被其他线程获取，则进行等待
  - 不会像synchronized一样在异常时自动释放锁，在finally中释放锁，保证异常时锁一定被释放
  - lock()方法不能被中断，会有很大隐患，一旦陷入死锁lock()就会陷入永久等待
- tryLock()：用来尝试获取锁，如果当前锁没有被其他线程占用，则获取成功返回true，失败返回false
  - 相比lock，我们可以根据是否获取到锁来决定后续程序的行为，该方法会立即返回即便拿不到锁时也不会一直在那等
- tryLock(long time, TimeUnit unit)：设置超时时间，在设定时间内拿到锁返回true，时间到仍没拿到则返回false
- lockInterruptibly()：相当于tryLock(long time, TimeUnit unit)把超时时间设置为无限，等待锁过程可被中断
- unlock()：解锁方法，按照规范进行释放，try - finally
#### 锁的分类
- 线程要不要锁住同步资源：悲观锁(互斥同步锁)--乐观锁(非互斥同步锁)
  - 互斥同步锁劣势：阻塞和唤醒带来的性能劣势、永久阻塞、优先级反转
  - Java中悲观锁实现就是synchronized和Lock相关类
  - 乐观锁在更新过程中去对比在我修改期间数据有没有被改变过，一般利用CAS算法实现
- 多线程能否共享一把锁：共享锁--独占锁
- 多线程竞争时，是否排队：公平锁--非公平锁
- 同一个线程是否可以重复获取一把锁：可重入锁--不可重入锁
- 是否可中断：可中断锁--非可中断锁
- 等锁过程：自旋锁--非自旋锁
