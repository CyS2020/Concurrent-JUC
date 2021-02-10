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
  - Java中悲观锁实现就是synchronized和Lock相关类，原子类、并发容器等为乐观锁
  - 乐观锁在更新过程中去对比在我修改期间数据有没有被改变过，一般利用CAS算法实现
  - 悲观锁：并发写入多，临界区持锁时间长，避免大量无用自旋的操作
    - 临界区有IO操作
    - 临界区代码复杂或者循环量大
    - 临界区竞争非常激烈
  - 乐观锁：并发写入少，大部分是读取场景，不加锁让读取性能大幅提高
- 多线程能否共享一把锁：共享锁--独占锁
  - Java中ReentrantReadWriteLock读写锁，读锁为共享锁，写锁为独享锁
  - 要么多个线程同时拥有读锁，要么一个线程有写锁(要么多读，要么一写)
  - 公平情况下不允许插队；非公平下允许写锁随时插队，读锁仅在等待队列头不是想获取写锁的线程的时候可以许插队
  - 支持锁的降级，不支持锁的升级，锁降级提高效率，上述读写锁不支持升级是因为死锁
- 多线程竞争时，是否排队：公平锁--非公平锁
  - 非公平锁避免唤醒带来的空档期，提高效率
  - 针对tryLock()方法，它是很猛的，它不遵守设定的公平的规则
- 同一个线程是否可以重复获取一把锁：可重入锁--不可重入锁
  - 可重入性能够避免死锁，提高了封装性，synchronized和ReentrantLock都是可重入锁
- 是否可中断：可中断锁--非可中断锁
  - Java中synchronized是不可中断锁，Lock是可中断锁，tryLock(time)和lockInterruptibly都能响应中断
- 等锁过程：自旋锁--阻塞锁
  - 让当前线程稍等一下，等前面的同步资源线程释放锁，当前线程无需阻塞直接获取同步资源，避免线程切换开销
  - 如果前面同步资源占用时间长，那么自旋线程只会白白浪费处理器资源
  - 自旋锁起始开销低于悲观锁，但是随着自旋时间的增长，开下也是线性增长的
  - 适用于多和服务器，且并发度不是特别高；适用于临界区比较短小的情况(一旦拿到锁很久才释放那就不适合)
#### JVM锁优化
- 自旋锁和自适应；锁消除；锁粗化
#### 编码优化锁提高并发性能
- 缩小同步代码块
- 尽量不要锁住方法
- 减少锁的次数
- 避免人为制造"热点"
- 锁中尽量不要包含锁
- 选择合适的锁或者合适的工具类
### 原子类
#### 相比于锁的特点
- 粒度更细：原子变量可以吧竞争范围缩小到变量级别，这是我们可以获得的最细粒度的情况，通常锁粒度要大于变量粒度
- 效率更高：通常使用原子类的效率会比使用锁的效率更高，除了高度竞争的情况
#### 六类原子类
- Atomic* 基本类型原子类：AtomicInteger、AtomicLong、AtomicBoolean
- Atomic* Array 数组类型原子类：AtomicIntegerArray、AtomicLongArray、AtomicReferenceArray
- Atomic* Reference 引用类型原子类：AtomicReferenece、AtomicStampedReference、AtomicMarkableReference
- Atomic* FieldUpdater 升级类型原子类：AtomicIntegerFieldUpdater、AtomicLongFieldUpdater、AtomicReferenceFieldUpdater
  - 所升级的类中的成员变量的可见性不能是private，不能升级静态变量static修饰的
- Adder累加器：LongAdder、DoubleAdder
  - 分段累加，内部有一个base变量和一个Cell[]数组共同参与计数
  - base变量：竞争不激烈，直接累加到该变量上
  - Cell[]数组：竞争激烈，各个线程分散累加到自己的槽Cell[i]中
  - LongAdder适合场景是统计求和计数的场景
- Accumulator累加器：LongAccumulator、DoubleAccumulator
  - Accumulator适合需要大量计算，且需要并行计算；计算顺序不能有要求
#### CAS原理
- CAS有三个操作数：内存值V、预期值A、要修改的值B，当且仅当预期值A和内存值V相同时，才将内存值V修改为B，否则什么都不做返回现在的V值
- 缺点：ABA问题-添加版本号、自旋时间过长，适应于资源竞争不激烈的场景，避免过度消耗CPU资源
### final关键字和不变性
- 防止类被继承：不可被继承，最常见的final类是String
- 防止方法被重写：不能修饰构造函数，被修饰的方法不能被override，(引申：static方法不能被重写，但可以重名)
- 防止变量被修改：被final修饰的变量，意味着值不能被修改，引用不能变
#### 变量赋值时机
- final instance variable
  - 第一种是在声明变量的等号右边直接赋值
  - 第二种就是在构造函数中赋值
  - 第三种就是在类的初始化代码块中赋值(不常见)
- final static variable
  - 第一种是在声明变量的等号右边直接赋值
  - 第二种使用static初始化代码块赋值，但是不能用普通的初始化代码块赋值
- final local variable
  - 没有规定赋值时机，只要求在使用前必须赋值，这个方法中的非final变量的要求是一样的
#### 栈封闭
- 方法里新建的局部变量，实际上是存储在每个线程私有的栈空间，而每个栈空间是不能被其他线程所访问到的
  所以不会有线程安全问题。这就是著名的"栈封闭"技术，是"线程封闭"技术的一种情况
### 并发容器
#### ConcurrentHashMap
- 线程安全的HashMap
#### CopyOnWriteArrayList
- 线程安全的ArrayList
#### BlockingQueue
- 这是一个接口，表示阻塞队列，非常适合用于作为数据共享的通道
#### ConcurrentLinkedQueue
- 高效的非阻塞并发队列，使用链表实现，可以看做一个线程安全的LinkedList
#### ConcurrentSkipListMap
- 是一个Map，使用调表的数据结构进行快速查找
