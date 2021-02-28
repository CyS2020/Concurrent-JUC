### 一、线程池
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
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E6%B7%BB%E5%8A%A0%E7%BA%BF%E7%A8%8B%E8%A7%84%E5%88%99%20%20.png" width = "600" height = "250" alt="线程池添加规则" align=center /><br/>
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
- TIDYING：所有任务都已终止，workerCount为零时线程会切换到该状态，并允许terminated()钩子方法运行
- TERMINATED：terminated()运行完成
### 二、ThreadLocal
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
- get方法中先是取出当前线程并取出线程的ThreadLocalMap，然后调用map.getEntry方法，把本ThreadLocal的引用即this作为参数传入，取出map中属于本ThreadLocal的value<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/ThreadLocal%E5%8E%9F%E7%90%86%E5%9B%BE.png" width = "600" height = "300" alt="ThreadLocol原理图" align=center /><br/>
#### ThreadLocal主要方法
- T initialValue()：初始化
- void set(T t)：为线程设置一个新值
- T get()：得到这个线程对应的value，如果首次调用则调用initialValue来得到这个值
- void remove()：删除对应这个线程的值
#### ThreadLocal注意点
- 内存泄漏
  - 调用链：Thread -> ThreadLocalMap -> Entry(key为null) -> value
  - 正常情况下，当线程终止，线程会被回收，里面的变量也会被回收包括ThreadLocalMap等，但是如果线程不终止(例如线程池)，会有如上调用链，key会被回收所以为null，value则不会；图中的ThreadLocal Ref == Key<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/20180523190740878.png" width = "600" height = "300" alt="ThreadLocol回收原理" align=center /><br/>
- 空指针异常
  - 在get之前如果不进行初始化，会返回null，注意基本数据类型的装箱拆箱导致空指针异常
- 共享对象
  - 如果每个线程中ThreadLocal.set()的数据是多线程共享的同一个对象，那么多个线程的ThreadLocal.get()取得的数据还是共享对象本身，还是有并发访问的问题，例如不应该在ThreadLocal中放入静态的对象
- 如果可以不使用ThreadLocal就能解决问题，就不要强行使用ThreadLocal
- 优先使用框架的支持，而不是自己创造
  - 在Spring中，如果可以使用RequsetContextHolder，那么就不需要自己维护ThreadLocal
- 每次http请求都对应一个线程，线程之间互相隔离，这就是ThreadLocal的典型应用场景
### 三、锁(Lock)
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
  - 公平情况下不允许插队；非公平下允许写锁随时插队，读锁仅在等待队列头不是想获取写锁的线程的时候允许插队
  - 支持锁的降级，不支持锁的升级，锁降级提高效率，上述读写锁不支持升级是因为死锁。因为不能有读又有写，想升级要等待所有读锁释放，假设有两个锁要升级，则需要等待对方释放从而造成死锁
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
  - 自旋锁起始开销低于悲观锁，但是随着自旋时间的增长，开销也是线性增长的
  - 适用于多和服务器，且并发度不是特别高；适用于临界区比较短小的情况(一旦拿到锁很久才释放那就不适合)
#### JVM锁优化
- 自旋锁和自适应；锁消除；锁粗化
- synchronized锁升级：无锁 -> 偏向锁 -> 轻量级锁(CAS) -> 重量级锁(OS)
#### 编码优化锁提高并发性能
- 缩小同步代码块
- 尽量不要锁住方法
- 减少锁的次数
- 避免人为制造"热点"
- 锁中尽量不要包含锁
- 选择合适的锁或者合适的工具类
### 四、原子类
#### 相比于锁的特点
- 粒度更细：原子变量可以把竞争范围缩小到变量级别，这是我们可以获得的最细粒度的情况，通常锁粒度要大于变量粒度
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
### 五、final关键字和不变性
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
### 六、并发容器
#### HashMap线程不安全
- 同时put碰撞导致数据丢失
- 同时put扩容导致数据丢失
- 死循环造成的CPU利用率100%，多线程同时扩容的时候造成循环链表导致的1.7
- 常规集合迭代时不允许修改(add、put、remove等)，集合大小发生变化会引发ConcurrentModificationException异常
- Map中要求的key为不可变对象
#### ConcurrentHashMap
- 线程安全的HashMap
- 组合方法有可能不是线程安全的，一使用Synchronized来加锁，二使用自带的组合操作replace、putIfAbsent等
- 为什么是8来转为红黑树：红黑树存贮空间是链表的两倍，正常情况下链表的长度不会达到8(概率极低)，如果出现了说明哈希算法有问题
- 数据结构如图所示1.7 -> 1.8<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/JDK1.7%E7%9A%84ConcurrentHashMap%E5%AE%9E%E7%8E%B0%E5%92%8C%E5%88%86%E6%9E%90%202.png" width = "400" height = "300" alt="JDK1.7的ConcurrentHashMap实现和分析" align=center /><img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/JDK1.8%E7%9A%84ConcurrentHashMap%E5%AE%9E%E7%8E%B0%E5%92%8C%E5%88%86%E6%9E%90%201.png" width = "400" height = "300" alt="JDK1.8的ConcurrentHashMap实现和分析" align=center /><br/>
#### CopyOnWriteArrayList
- 线程安全的ArrayList
- Vector和SynchronizedList的锁力度太大，并发效率低，并且迭代的时候无法编辑
- Copy-On-Write表示写的时候Copy一份数据。并发容器还包括CopyOnWriteArraySet，用来代替同步Set
- 适用场景：读操作可以尽可能地快，而写操作即使慢一些也没有太大关系
  - 读多写少：黑名单、白名单，每日更新
  - 监听器：迭代操作远多于修改操作
- 读取完全不用加锁，写入也不会阻塞读的操作，只有写入与写入之间需要进行同步等待--创建副本，读写分离 + "不可变"原理(对于旧容器)
- 迭代器数据取决于迭代器生成的时机迭代的数据可能会过期；迭代时可以进行修改(add、put、remove)，不会引发ConcurrentModificationException异常
- 此类容器缺点：
  - 数据一致性问题：CopyOnWrite容器只能保证数据的最终一致性，不能保证数据的实时一致性，若希望写入的数据马上能读到切勿使用该容器
  - 内存占用问题：因为CopyOnWrite的写是复制机制，所以写操作时候，内存里会同时驻扎两个对象的内存
#### BlockingQueue
- 这是一个接口，表示阻塞队列，非常适合用于作为数据共享的通道
- 各并发队列关系图<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E9%98%BB%E5%A1%9E%E9%98%9F%E5%88%97%E5%92%8C%E9%9D%9E%E9%98%BB%E5%A1%9E%E9%98%9F%E5%88%97%20.png" width = "750" height = "250" alt="阻塞队列与非阻塞队列" align=center /><br/>
- 阻塞队列是具有阻塞功能的队列，阻塞队列的一端是给生产者放数据用的，另一端是给消费者拿数据用的；阻塞队列是线程安全的
- 阻塞功能：take()方法若无数据则阻塞直到有数据，put()方法若队列已满则阻塞直到有空闲空间
- 主要方法：put-take阻塞；add-remove-element异常；offer-poll-peek返回；
  - ArrayBlockingQueue：1.有界，2.公平；
  - LinkedBlockingQueue：1.无界Integer.MAX_VALUE，2.put锁+take锁
  - PriorityBlockingQueue：1.无界Integer.MAX_VALUE，2.自然排序
  - SynchronousQueue：1.容量0，2.无peek等函数
  - DelayQueue：1.无界，2.时间排序
  - ConcurrentLinkedQueue：1.非阻塞，2.CAS实现
#### ConcurrentLinkedQueue
- 高效的非阻塞并发队列，使用链表实现，可以看做一个线程安全的LinkedList
#### ConcurrentSkipListMap
- 是一个Map，使用调表的数据结构进行快速查找
### 七、控制并发流程
#### CountDownLatch倒计时门闩
- CountDownLatch(int count)：仅有一个构造函数，参数count为需要倒数的数值
- await()：调用await()方法的线程会被挂起，它会等待直到count为0才继续执行
- countDown()：将count值减1，直到为0时，等待线程会被唤醒
- 适用场景：一等多，多等一，多等多(不常见)、CountDownLatch不能够重复使用
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/CountDownLatch%201.png" width = "500" height = "360" alt="阻塞队列与非阻塞队列" align=center /><br/>
#### Semaphore信号量
- 用来限制或管理数量有限的资源的使用场景
- new Semaphore(int permits, boolean fair)：如果传入true需要排队等待资源
- acquire()、acquireUninterruptibly()、tryAcquire()、tryAcquire(int timeout)
- release()归还许可证
- 一次性可以获取或释放多个许可证，获取与释放必须一致
- 获取和释放许可证对线程并无要求，也许是A线程获取B线程释放，逻辑合理即可
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E4%BF%A1%E5%8F%B7%E9%87%8F1.png" width = "600" height = "300" alt="阻塞队列与非阻塞队列" align=center /><br/>
#### Condition接口(条件对象)
- 如果说Lock是用来代替synchronized，那么Condition就是用来代替相对应的Object.wait()/notify()
- Object.notify()无法唤醒指定线程；使用Lock + Condition 实现唤醒指定的部分线程，或者使用LockSupport唤醒指定线程
- await()方法会自动释放持有的锁，调用await()的时候必须先持有锁<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/Condition%E4%BD%9C%E7%94%A8%20.png" width = "500" height = "300" alt="Condition条件接口" align=center /><br/>
#### CyclicBarrier循环栅栏
- CountDownLatch用于时间，而CyclicBarrier是用于线程的
- CountDonwLatch不能重复使用，CyclicBarrier可以重复使用
### 八、AQS(AbstractQueuedSynchronizer)原理
#### AQS介绍
- Semaphore内部有一个Sync类，Sync类继承了AQS，CountDownLatch也是一样
- AQS是一个用于构建锁、同步器、协作工具类的工具类(框架)，有了AQS，构建线程协作类就容易多了<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/AQS%201.png" width = "400" height = "270" alt="内部实现AQS" align=center /><br/>
#### state状态
- 会根据具体实现类的不同而不同
- 比如Semaphore表示"剩余许可证数量"，CountDownLatch里表示还需要"倒数的数量"，ReentrantLock中state表示锁的占有情况(0-不被占有，>=1-被占有)，包括可重入计数
- state是volatile修饰的，会被并发地修改，所以所有修改state方法都需要保证线程安全，getState、setState、compareAndSetState方法
#### 控制线程抢锁和配合的FIFO队列
- 用来存放等待的线程，AQS就是"排队管理器"，当多个线程争用同一把锁时必须有排队机制将那些没拿到锁的线程串在一起，当锁释放时锁管理器就会挑选一个合适的线程来占有这个刚刚释放的锁
- AQS会维护一个等待线程的队列，把线程都方法到这个队列里(双向链表)<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/AQS%203.png" width = "700" height = "150" alt="等待队列" align=center /><br/>
#### 期望协作工具类去实现的获取/释放等重要方法
- 这里获取和释放的方法，需要协作类自己实现，并且含义各不相同
- 获取方法：依赖于state变量，经常会阻塞(比如获取不到锁的情况)
  - 在Smeaphore中，获取就是acquire方法，作用是获取一个许可证，state--
  - 在CountDownLatch里，获取就是await方法，作用是"等待，直到倒数结束"，判断state=0
  - 在ReentrantLock里，获取就是lock，state=1，重入的时候state++
- 释放方法：不会阻塞
  - 在Semaphore中，释放就是release方法，作用是释放一个许可证，state++
  - 在CountDownLatch里，释放就是countDown方法，作用是"倒数一个数"，state--
  - 在ReentrantLock里，释放就是unlock(减完后state=0时没有线程占用)，state--
#### AQS实现用法
- 第一步：写一个类，想好协作的逻辑，实现获取/释放的方法，内部调用Sync中的方法
- 第二步：内部写一个Sync类继承AbstractQueuedSynchronizer
- 第三步：根据独占来重写里面的tryAcquire/tryRelease，共享来重写tryAcquireShared/tryReleaseShared等方法，在之前的获取/释放中调用它们<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/CountDownLatch%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90%20.png" width = "400" height = "270" alt="CountDownLatch源码继承关系" align=center /><br/>
### 九、获取子线程的结果
#### Runnable不足之处
- 第一：不能返回一个返回值，第二：不能抛出checked Exception异常
- Callable接口：V call() throws Exception; 解决了这两个问题
- Future + Callable ————治理线程的第二法宝
#### Future类
- Callable与Future的关系
  - 可以通过Future.get()来获取Callable接口返回的执行结果，Future.isDone()还能判断任务是否执行结束
  - 在call()未执行完毕之前，调用get()的线程会被阻塞，直到call()方法返回结果，get()才会得到该结果，然后线程切换到RUNNABLE状态
  - Future是一个存储器，存储了call()这个任务的结果，任务执行时间无法确定
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E7%9A%84submit%E6%96%B9%E6%B3%95%E8%BF%94%E5%9B%9EFuture%E5%AF%B9%E8%B1%A1.png" width = "600" height = "330" alt="线程池的submit方法返回Future对象1" align=center /><br/>
#### get()获取结果
- 任务正常完成：get方法会立刻返回结果
- 任务尚未完成：get将阻塞并直到任务完成(任务还没开始或进行中)
- 任务执行中抛出Exception：get方法会抛出ExecutionException异常，是call方法执行时产生的，无论call执行时抛出的异常类型是啥，最够get方法抛出的异常都是ExecutionException
- 任务被取消：get方法会抛出CancellationException
- 执行任务超时：get方法有一个重载方法，传入延迟时间，时间到了未获得结果抛出TimeoutException；
#### cancel方法
- 超时不获取，任务需取消-cancel()
- 任务还没有开始执行，任务会被正常取消，未来也不会执行，方法返回true
- 任务已经完成，或者已经取消，cancel()方法会执行失败，方法返回false
- 如果这个任务已经开始执行了，那么这个取消方法将不会直接取消该任务，取决于cancel入参
- Future.cancel(true)：任务能够处理interrupt
- Future.cancel(false)：仅用于避免启动尚未启动的任务
  - 未能处理interrupt的任务
  - 不清楚任务是否支持取消
  - 需要等待已经开始的任务执行完成
#### FutureTask类
- 既可以作为Runnable被线程执行，又可以作为Future得到Callable的返回值<br/>
<img src="https://github.com/CyS2020/Concurrent-JUC/blob/main/src/main/resources/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E7%9A%84submit%E6%96%B9%E6%B3%95%E8%BF%94%E5%9B%9EFuture%E5%AF%B9%E8%B1%A12.png" width = "260" height = "240" alt="线程池的submit方法返回Future对象2" align=center /><br/>
### 十、总结
#### JUC的主要内容
- 线程安全(互斥，非互斥，无锁)
- 线程管理(线程池，Future+Callable)
- 线程协作(wait+notify，lock+condition，LockSupport)
#### 实战必会
- 双线程交替打印奇偶数
- 生产者消费者模式
- 3种单例设计模式
- 哲学家就餐问题模拟死锁
- 自己实现自旋锁
