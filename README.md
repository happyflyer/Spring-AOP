# [Spring-AOP](https://github.com/happyflyer/Spring-AOP)

- [探秘 Spring AOP](https://www.imooc.com/learn/869)

AOP 是什么？

- 是一种编程范式，不是编程语言
- 解决特定问题，不能解决所有问题
- 是 OOP 的补充，不是替代

编程范式：

- 面向过程编程
- 面向对象编程
- 函数式编程
- 事件驱动编程
- 面向切面编程
- ...

AOP 的初衷：

- DRY: Don't Repeat yourself
- SoC: Separation of concerns
  - 水平分离：展示层->服务层->持久层
  - 垂直分离：模块划分订单、库存等）
  - 切面分离：分离功能性需求与非功能性需求

使用 AOP 的好处：

- 集中处理某一关注点/横切逻辑
- 可以很方便地添加/删除关注点
- 侵入性少，增强代码可读性及可维护性
- ...

AOP 的应用场景：

- 权限控制
- 缓存控制
- 事务控制
- 审计日志
- 性能监控
- 分布式追踪
- 异常处理
- ...

支持 AOP 的编程语言：

- Java
- .NET
- C/C++
- Ruby
- Python
- PHP
- ....

## 1. 实现一个权限控制

- 实现权限切换

```java
public class CurrentUserHolder {
    public static final ThreadLocal<String> holder = new ThreadLocal<>();
    public static String get() {
        return holder.get() == null ? "unknown" : holder.get();
    }
    public static void set(String user) {
        holder.set(user);
    }
}
```

- 实现认证逻辑

```java
@Service
public class AuthService {
    public void checkAccess() {
        System.out.println("执行了checkAccess...");
        String user = CurrentUserHolder.get();
        if (!"admin".equals(user)) {
            throw new RuntimeException("operation not allow");
        }
    }
}
```

- 实现业务逻辑

```java
public class ProductService {
    public void insert(Product product) {
        System.out.println("insert product");
    }
    public void delete(Long id) {
        System.out.println("delete product");
    }
}
```

### 1.1. 传统方式

```java
@SpringBootConfiguration
@ComponentScan("org.example.spring_aop.prepare")
public class PrepareApplication {
}
```

```java
@Service
public class AuthService {
    // ...
}
```

```java
@Service
public class ProductService {
    @Autowired
    AuthService authService;
    public void insert(Product product) {
        authService.checkAccess();
        // ...
    }
    public void delete(Long id) {
        authService.checkAccess();
        // ...
    }
}
```

### 1.2. AOP 方式

```java
@SpringBootConfiguration
@ComponentScan("org.example.spring_aop.teach")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class TeachApplication {
}
```

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOnly {
}
```

```java
@Aspect
@Component
public class SecurityAspect {
    @Autowired
    AuthService authService;
    @Pointcut("@annotation(AdminOnly)")
    public void adminOnly() {
    }
    @Before("adminOnly()")
    public void check() {
        authService.checkAccess();
    }
}
```

```java
@Service
public class AuthService {
    // ...
}
```

```java
@Service
public class ProductService {
    @AdminOnly
    public void insert(Product product) {
        // ...
    }
    @AdminOnly
    public void delete(Long id) {
        // ...
    }
}
```

### 1.3. 单元测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    ProductService productService;
    @Test(expected = RuntimeException.class)
    public void testDelete() {
        CurrentUserHolder.set("tom");
        productService.delete(1L);
    }
    @Test
    public void testInsert() {
        CurrentUserHolder.set("admin");
        productService.insert(new Product());
    }
}
```

## 2. 使用详解

- `@Aspect`
- `@Pointcut(expression)`
- Advice

### 2.1. 切面表达式

- designators（描述符）
  - 匹配方法
    - `execution()`
  - 匹配注解
    - `@target()`
    - `@args()`
    - `@within()`
    - `@annotation()`
  - 匹配包、类型
    - `within()`
  - 匹配对象
    - `@this()`
    - `@bean()`
    - `@target()`
  - 匹配参数
    - `@args()`
- wildcard（通配符）
  - `*`：匹配任意数量的字符
  - `+`：匹配指定类及其子类
  - `..`：一般用于匹配任意数的子包或参数
- operator（运算符）
  - `&&`：与操作符
  - `||`：或操作符
  - `!`：非操作符

#### 2.1.1. 匹配方法

```java
execution（
    modifier-pattern?
    ret-type-pattern
    declaring-type-pattern?
    name-pattern(param-pattern)
    throws-pattern?
)
```

```java
// 匹配service包下所有以Service结尾的类的所有方法
@Pointcut("execution(public * org.example.spring_aop.aspect.service.*Service.*(..))")
// 匹配service包及子包下所有以Service结尾的类的所有方法
@Pointcut("execution(public * org.example.spring_aop.aspect.service..*Service.*(..))")
// 匹配service包下所有以Service结尾的类中返回类型为String的方法
@Pointcut("execution(public String org.example.spring_aop.aspect.service.*Service.*(..))")
// 匹配service包下所有以Service结尾的类中无返回的方法
@Pointcut("execution(public void org.example.spring_aop.aspect.service.*Service.*(..))")
// 匹配service包下所有以Service结尾的类中无参的方法
@Pointcut("execution(public * org.example.spring_aop.aspect.service.*Service.*())")
// 匹配service包下所有以Service结尾的类中只有一个参数且为Long类型的方法
@Pointcut("execution(public * org.example.spring_aop.aspect.service.*Service.*(Long))")
// 匹配service包下所有以Service结尾的类中抛出IllegalAccessException异常的方法
@Pointcut("execution(public * org.example.spring_aop.aspect.service.*Service.*(..) throws java.lang.IllegalAccessException)")
```

#### 2.1.2. 匹配包/类型

```java
// 匹配ProductService类里头的所有方法
@Pointcut("within(org.example.spring_aop.aspect.service.ProductService)")
// 匹配sub包下所有类的方法
@Pointcut("within(org.example.spring_aop.aspect.service.sub.*)")
// 匹配service包及子包下所有以Service结尾的类的所有方法
@Pointcut("within(org.example.spring_aop.aspect.service..*Service)")
```

#### 2.1.3. 匹配对象

```java
// 匹配AOP对象的目标对象为指定类型的方法，即Loggable的aop代理对象的方法
@Pointcut("this(org.example.spring_aop.aspect.log.Loggable)")
// 匹配实现Loggable接口的目标对象的方法，即Loggable实现类的方法
@Pointcut("target(org.example.spring_aop.aspect.log.Loggable)")
// 匹配所有以Service结尾的bean里面的方法
@Pointcut("bean(*Service)")
```

#### 2.1.4. 匹配参数

```java
// 匹配只有一个参数且为Long类型的方法
@Pointcut("args(Long)")
@Pointcut("execution(* *..*(Long))")
// 匹配第一个参数为Long类型的方法
@Pointcut("args(Long,..)")
// 匹配只有两个参数且分别为Long、String类型的方法
@Pointcut("args(Long,String)")
```

#### 2.1.5. 匹配注解

```java
// 匹配有AdminOnly注解的方法
@Pointcut("@annotation(org.example.spring_aop.aspect.annotation.AdminOnly)")
// 匹配有NeedSecured注解的类中的所有方法
@Pointcut("@within(org.example.spring_aop.aspect.annotation.NeedSecured)")
// 匹配有NeedSecured注解的类中的所有方法，要求注解的RetentionPolicy必须为RUNTIME
@Pointcut("@target(org.example.spring_aop.aspect.annotation.NeedSecured)")
// 匹配传入参数类型有NeedSecured注解的方法
@Pointcut("@args(org.example.spring_aop.aspect.annotation.NeedSecured)")
```

### 2.2. Advice 注解

```java
@SpringBootConfiguration
@ComponentScan("org.example.spring_aop.advice")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AdviceApplication {
}
```

```java
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminOnly {
}
```

- 业务逻辑

```java
@Service
public class ProductService {
    public String findProductNameById(Long id) {
        System.out.println("execute getProductNameById(id=" + id + ")");
        return "product name";
    }
    public void exceptionMethod(String arg) throws IllegalAccessException {
        System.out.println("execute exceptionMethod(arg=" + arg + ")");
        throw new IllegalAccessException("one exception");
    }
    @AdminOnly
    public void addProduct(Long id, String name) {
        System.out.println("execute addProduct(id=" + id + ", name=" + name + ")");
    }
    @AdminOnly
    public void deleteProduct(Long id) {
        System.out.println("execute deleteProduct(id=" + id + ")");
    }
    public void loopUpProduct(Long id) {
        System.out.println("execute loopUpProduct(id=" + id + ")");
    }
}
```

- 单元测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @Autowired
    ProductService productService;
    @Test
    public void testExecution() {
        productService.findProductNameById(1L);
        System.out.println("==========");
        try {
            productService.exceptionMethod("arg");
        } catch (IllegalAccessException ignored) {
        }
        System.out.println("==========");
        productService.addProduct(2L, "name2");
        System.out.println("==========");
        productService.deleteProduct(1L);
        System.out.println("==========");
        productService.loopUpProduct(2L);
    }
}
```

#### 2.2.1. After

```java
@Pointcut("@annotation(org.example.spring_aop.advice.annotation.AdminOnly)")
public void matchAnnotation() {
}
```

```java
@After("matchAnnotation()")
public void advice1() {
    System.out.println("###after matchAnnotation");
}
```

```java
execute getProductNameById(id=1)
==========
execute exceptionMethod(arg=arg)
==========
execute addProduct(id=2, name=name2)
###after matchAnnotation
==========
execute deleteProduct(id=1)
###after matchAnnotation
==========
execute loopUpProduct(id=2)
```

```java
@Pointcut("execution(* *..*(Long))")
public void matchLongArg() {
}
```

```java
@After("matchLongArg()")
public void advice2() {
    System.out.println("###after matchLongArg");
}
```

```java
execute getProductNameById(id=1)
###after matchLongArg
==========
execute exceptionMethod(arg=arg)
==========
execute addProduct(id=2, name=name2)
==========
execute deleteProduct(id=1)
###after matchLongArg
==========
execute loopUpProduct(id=2)
###after matchLongArg
```

#### 2.2.2. AfterThrowing

```java
@Pointcut("execution(* *..*(*) throws java.lang.IllegalAccessException)")
public void matchException() {
}
```

```java
@AfterThrowing("matchException()")
public void advice3() {
    System.out.println("###afterThrowing matchException");
}
```

```java
execute getProductNameById(id=1)
==========
execute exceptionMethod(arg=arg)
###afterThrowing matchException
==========
execute addProduct(id=2, name=name2)
==========
execute deleteProduct(id=1)
==========
execute loopUpProduct(id=2)
```

#### 2.2.3. AfterReturning

```java
@Pointcut("execution(String *..*.*(..))")
public void matchReturn() {
}
```

```java
@AfterReturning(value = "matchReturn()", returning = "result")
public void advice4(Object result) {
    System.out.println("###afterReturning matchReturn, result = " + result);
}
```

```java
execute getProductNameById(id=1)
###afterReturning matchReturn, result = product name
==========
execute exceptionMethod(arg=arg)
==========
execute addProduct(id=2, name=name2)
==========
execute deleteProduct(id=1)
==========
execute loopUpProduct(id=2)
```

#### 2.2.4. Around

```java
@Pointcut("execution(* *..*(Long))")
public void matchLongArg() {
}
```

```java
@Around("matchLongArg()")
public Object advice5(ProceedingJoinPoint joinPoint) {
    System.out.println("###around before");
    Object result = null;
    try {
        result = joinPoint.proceed(joinPoint.getArgs());
        System.out.println("###around after returning, result = " + result);
    } catch (Throwable throwable) {
        System.out.println("###around after throwing");
    } finally {
        System.out.println("###around finally");
    }
    return result;
}
```

```java
###around before
execute getProductNameById(id=1)
###around after returning, result = product name
###around finally
==========
execute exceptionMethod(arg=arg)
==========
execute addProduct(id=2, name=name2)
==========
###around before
execute deleteProduct(id=1)
###around after returning, result = null
###around finally
==========
###around before
execute loopUpProduct(id=2)
###around after returning, result = null
###around finally
```

#### 2.2.5. Before

```java
@Pointcut("execution(* *..*(Long))")
public void matchLongArg() {
}
```

```java
@Before("matchLongArg() && args(productId)")
public void advice6(Long productId) {
    System.out.println("###before, arg = " + productId);
}
```

```java
###before, arg = 1
execute getProductNameById(id=1)
==========
execute exceptionMethod(arg=arg)
==========
execute addProduct(id=2, name=name2)
==========
###before, arg = 1
execute deleteProduct(id=1)
==========
###before, arg = 2
execute loopUpProduct(id=2)
```

### 2.3. Advice 注解的顺序关系

- 业务逻辑

```java
@Component
public class LogService {
    public String log(Long id) throws IllegalAccessException {
        Long exceptionId = 2L;
        System.out.println("log from LogService.log");
        if (id.equals(exceptionId)) {
            return "log content: " + id;
        } else {
            throw new IllegalAccessException("exception in LogService.log");
        }
    }
}
```

- 定义切面

```java
@Pointcut("execution(* *..log(..))")
public void matchLogMethod() {
}
```

- 单元测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class LogServiceTest {
    @Autowired
    LogService logService;
    @Test
    public void testExecution() {
        String result = null;
        try {
            result = logService.log(1L);
            System.out.println("result = " + result);
        } catch (IllegalAccessException ignored) {
        }
        System.out.println("==========");
        try {
            result = logService.log(2L);
            System.out.println("result = " + result);
        } catch (IllegalAccessException ignored) {
        }
    }
}
```

#### 2.3.1. 没有 Around 注解

```java
###before, arg = 1
log from LogService.log
###afterThrowing matchException
###after matchLongArg
==========
###before, arg = 2
log from LogService.log
###afterReturning matchReturn, result = log content: 2
###after matchLongArg
result = log content: 2
```

#### 2.3.2. 有 Around 注解

```java
###around before
###before, arg = 1
log from LogService.log
###around after throwing
###around finally
###afterReturning matchReturn, result = null
###after matchLongArg
result = null
==========
###around before
###before, arg = 2
log from LogService.log
###around after returning, result = log content: 2
###around finally
###afterReturning matchReturn, result = log content: 2
###after matchLongArg
result = log content: 2
```

### 2.4. 小结

Pointcut 表达式组成：

- 切面表达式
  - designators（描述符）
    - `@execution`
    - `@within`
    - `@target`
    - `@args`
    - `@annotation`
  - wildcards（通配符）
  - operator（操作符）
- Advice 注解
  - `@Before`
  - `@After`
  - `@AfterThrowing`
  - `@AfterReturning`
  - `@Around`

## 3. 实现原理

- 概述
- 设计
  - 代理模式
  - 责任链模式
- 实现
  - JDK 实现
  - Cglib 实现

### 3.1. 原理概述

织入的时机

- 编译期(AspectJ)
- 类加载时(AspectJ 5+)
- 运行时(Spring AOP) `√`

运行时织入

- 运行时织入是怎么实现的？
- 从静态代理到动态代理
- 基于接口代理和基于继承代理

### 3.2. 代理模式

![代理模式说明图](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/代理模式说明图.4sdigqid7va0.jpg)

![代理模式类图](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/代理模式类图.12kr2yycqf9c.jpg)

```java
public interface Subject {
    void request();
}
```

```java
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("real subject execute request");
    }
}
```

```java
public class ProxySubject implements Subject {
    private final Subject realSubject;
    public ProxySubject(Subject realSubject) {
        this.realSubject = realSubject;
    }
    @Override
    public void request() {
        System.out.println("before");
        try {
            this.realSubject.request();
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("finally");
        }
    }
}
```

```java
Subject subject = new ProxySubject(new RealSubject());
subject.request();
```

```java
before
real subject execute request
finally
```

### 3.3. jdk 代理

静态代理和动态代理

- 静态代理的缺点
- 动态代理的两类实现：基于接口的代理和基于继承的代理
- 两类实现的代理：jdk 代理和 cglib 代理

jdk 实现要点

- 类：`java.lang.reflect.Proxy`
- 接口：`InvocationHandler`
- 只能基于接口进行动态代理

```java
public interface Subject {
    void request();
    void hello();
}
```

```java
public class RealSubject implements Subject {
    @Override
    public void request() {
        System.out.println("real subject execute request");
    }
    @Override
    public void hello() {
        System.out.println("real subject execute hello");
    }
}
```

```java
public class JdkProxySubject implements InvocationHandler {
    private final Subject realSubject;
    public JdkProxySubject(Subject realSubject) {
        this.realSubject = realSubject;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        Object result;
        System.out.println("before");
        try {
            result = method.invoke(realSubject, args);
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("finally");
        }
        return result;
    }
}
```

```java
Subject subject = (Subject) Proxy.newProxyInstance(
        SubjectTest.class.getClassLoader(),
        new Class[]{Subject.class},
        new JdkProxySubject(new RealSubject())
);
subject.request();
subject.hello();
```

```java
before
real subject execute request
finally
before
real subject execute hello
finally
```

### 3.4. jdk 代理源码解析

- `Proxy.newProxyInstance`
- `getProxyClass0`、`ProxyClassFactory`、`ProxyGenerator`
- `newInstance`

#### 3.4.1. 设置保存生成的动态代理类

```java
// jdk1.8 及之前版本
System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
```

```java
// jdk11
System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
// System.getProperties().put("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
// -Djdk.proxy.ProxyGenerator.saveGeneratedFiles=true
```

#### 3.4.2. 动态代理类源码解析

```java
public final class $Proxy7 extends Proxy implements Subject {
    // ...
    private static Method m3;
    private static Method m4;
    public $Proxy7(InvocationHandler var1) throws  {
        super(var1);
    }
    // ...
}
```

```java
static {
    try {
        // ...
        m3 = Class.forName("org.example.spring_aop.proxy.jdk.Subject").getMethod("request");
        m4 = Class.forName("org.example.spring_aop.proxy.jdk.Subject").getMethod("hello");
    } catch (NoSuchMethodException var2) {
        throw new NoSuchMethodError(var2.getMessage());
    } catch (ClassNotFoundException var3) {
        throw new NoClassDefFoundError(var3.getMessage());
    }
}
```

```java
public final void request() throws  {
    try {
        super.h.invoke(this, m3, (Object[])null);
    } catch (RuntimeException | Error var2) {
        throw var2;
    } catch (Throwable var3) {
        throw new UndeclaredThrowableException(var3);
    }
}
public final void hello() throws  {
    try {
        super.h.invoke(this, m4, (Object[])null);
    } catch (RuntimeException | Error var2) {
        throw var2;
    } catch (Throwable var3) {
        throw new UndeclaredThrowableException(var3);
    }
}
```

### 3.5. cglib 动态代理

```java
public class DemoMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects,
                            MethodProxy methodProxy)
            throws Throwable {
        Object result;
        System.out.println("before in cglib");
        try {
            result = methodProxy.invokeSuper(o, objects);
        } catch (Exception e) {
            System.out.println("ex: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("finally in cglib");
        }
        return result;
    }
}
```

```java
Enhancer enhancer = new Enhancer();
enhancer.setSuperclass(RealSubject.class);
enhancer.setCallback(new DemoMethodInterceptor());
Subject subject = (Subject) enhancer.create();
subject.request();
subject.hello();
```

```java
before in cglib
real subject execute request
finally in cglib
before in cglib
real subject execute hello
finally in cglib
```

jdk 与 cglib 代理对比

1. jdk 只能针对有接口的类的接口方法进行动态代理
2. cglib 基于继承来实现代理，无法对 static、final 类进行代理
3. cglib 基于继承来实现代理，无法对 private、static 方法进行代理

### 3.6. Spring 对两种实现的选择

![Spring对代理的实现](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/Spring对代理的实现.2gisxaue96hw.jpg)

Spring 如何选择使用 jdk 代理还是 cglib 代理？

1. 如果目标对象实现了接口，默认使用 jdk 代理
2. 如果目标对象没有实现接口，默认使用 cglib 代理
3. 如果目标对象实现了接口，且强制使用 cglib 代理，则使用 cglib 代理

![Spring对两种实现的选择](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/Spring对两种实现的选择.2xbgybdr1mw0.jpg)

```java
@SpringBootApplication
// 强制试用 cglib 代理
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppDemoApplication.class, args);
    }
}
```

### 3.7. 责任链模式

![责任链模式类图](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/责任链模式类图.1brsdlqm5og0.jpg)

#### 3.7.1. 链式调用

```java
public abstract class Handler {
    private Handler successorHandler;
    public Handler getSuccessorHandler() {
        return successorHandler;
    }
    public void setSuccessorHandler(Handler successorHandler) {
        this.successorHandler = successorHandler;
    }
    public void execute() {
        this.handleProcess();
        if (successorHandler != null) {
            this.successorHandler.execute();
        }
    }
    protected abstract void handleProcess();
}
```

```java
public class HandlerA extends Handler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by A");
    }
}
```

```java
public class HandlerB extends Handler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by B");
    }
}
```

```java
public class HandlerC extends Handler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by C");
    }
}
```

```java
Handler handlerA = new HandlerA();
Handler handlerB = new HandlerB();
Handler handlerC = new HandlerC();
handlerA.setSuccessorHandler(handlerB);
handlerB.setSuccessorHandler(handlerC);
handlerA.execute();
```

```java
handle by A
handle by B
handle by C
```

#### 3.7.2. 使用 Chain

```java
public class Chain {
    private final List<ChainHandler> chainHandlers;
    private int index = 0;
    public Chain(List<ChainHandler> chainHandlers) {
        this.chainHandlers = chainHandlers;
    }
    public void proceed() {
        if (index >= this.chainHandlers.size()) {
            return;
        }
        chainHandlers.get(index++).execute(this);
    }
}
```

```java
public abstract class ChainHandler {
    public void execute(Chain chain) {
        this.handleProcess();
        chain.proceed();
    }
    protected abstract void handleProcess();
}
```

```java
public class ChainHandlerA extends ChainHandler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by A");
    }
}
```

```java
public class ChainHandlerB extends ChainHandler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by B");
    }
}
```

```java
public class ChainHandlerC extends ChainHandler {
    @Override
    protected void handleProcess() {
        System.out.println("handle by C");
    }
}
```

```java
List<ChainHandler> chainHandlers = Arrays.asList(
        new ChainHandlerA(),
        new ChainHandlerB(),
        new ChainHandlerC()
);
Chain chain = new Chain(chainHandlers);
chain.proceed();
```

```java
handle by A
handle by B
handle by C
```

![Spring责任链实现ReflectiveMethodInvocation源码](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/Spring责任链实现ReflectiveMethodInvocation源码.40az5ze5y8y0.jpg)

### 3.8. 小结

- 静态代理与动态代理
- 代理模式与责任链模式
- jdk 代理与 cglib 代理的区别及局限

## 4. 经典代码

Spring-AOP 在开源项目中应用

- Transactional 注解：事务
- PreAuthorize 注解：安全
- Cacheable 注解：缓存

### 4.1. Transactional 注解

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.4.3.RELEASE</version>
  <relativePath/>
</parent>
```

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>${spring.boot.version}</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.1</version>
  </dependency>
</dependencies>
```

```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://192.168.9.16:44060/spring_aop_transaction?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=MySQL@root123456
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL5Dialect
```

```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    // ...
}
```

```java
@Entity
public class OperationLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String content;
    private Date createdAt;
    // ...
}
```

```java
public interface UserDao extends JpaRepository<User, Long> {
}
```

```java
public interface OperationLogDao extends JpaRepository<OperationLog, Long> {
}
```

```java
@Component
public class DemoService {
    @Autowired
    UserDao userDao;
    @Autowired
    OperationLogDao operationLogDao;
    @Transactional
    public void addUser(String name) {
        OperationLog log = new OperationLog();
        log.setContent("create user:" + name);
        operationLogDao.save(log);
        User user = new User();
        user.setName(name);
        userDao.save(user);
    }
}
```

```java
@SpringBootApplication
public class TransactionDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionDemoApplication.class, args);
    }
}
```

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionDemoApplicationTest {
    @Autowired
    DemoService demoService;
    @Test
    public void testWithoutTransaction() {
        demoService.addUser("tom");
    }
}
```

- 第一次执行测试，插入日志，插入用户
- 第二次执行测试，由于无法插入用户，也没有插入日志

### 4.2. PreAuthorize 注解

安全校验 `@PreAuthorize`

- `MethodSecurityInterceptor`
- `PreInvocationAuthorizationAdviceVoter`
- `ExpressionBasedPreInvocationAdvice`

![PreAuthorize验证机制](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/PreAuthorize验证机制.6ap0af0e3o00.jpg)

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.4.3.RELEASE</version>
  <relativePath/>
</parent>
```

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>${spring.boot.version}</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic()
                .and()
                .logout()
                .permitAll();
    }
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("demo").password("demo").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN");
    }
}
```

```java
@RestController
public class DemoController {
    @RequestMapping("/index")
    public String index() {
        return "index";
    }
    @RequestMapping("/common")
    public String commonAccess() {
        return "only login can view";
    }
    @RequestMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin() {
        return "only admin can access";
    }
}
```

```java
@SpringBootApplication
public class SecurityDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoApplication.class, args);
    }
}
```

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityDemoApplicationTest {
    @Test
    public void contextLoads() {
    }
}
```

### 4.3. Cacheable 注解

缓存 `@Cacheable`

- `AnnotationCacheAspect`
- `CacheInterceptor`
- `CacheAspectSupport`

![Cacheable缓存机制](https://cdn.jsdelivr.net/gh/happyflyer/picture-bed@main/2021/Cacheable缓存机制.cz872iwa2k0.jpg)

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.4.3.RELEASE</version>
  <relativePath/>
</parent>
```

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>${spring.boot.version}</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

```java
@Component
public class MenuService {
    @Cacheable(cacheNames = {"menu"})
    public List<String> getMenuList() {
        System.out.println("mock:get from db");
        return Arrays.asList("article", "comment", "admin");
    }
}
```

```java
@SpringBootApplication
@EnableCaching
public class CacheDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheDemoApplication.class, args);
    }
}
```

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheDemoApplicationTests {
    @Autowired
    MenuService menuService;
    @Test
    public void testCache() {
        System.out.println("call:" + menuService.getMenuList());
        System.out.println("call:" + menuService.getMenuList());
    }
}
```

```java
mock:get from db
call:[article, comment, admin]
call:[article, comment, admin]
```

## 5. 案例实战

### 5.1. 实战案例背景和目标

- 商家产品管理系统
- 记录产品修改的操作记录
- 什么人在什么时间修改了哪些产品的哪些字段为什么值

### 5.2. 实现思路

- 利用 aspect 去拦截增删改方法
- 利用反射获取对象的新旧值
- 利用 `@Around` 的 advice 去记录操作记录

### 5.3. pom 依赖

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.4.3.RELEASE</version>
  <relativePath/>
</parent>
```

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>${mysql.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
    <version>${spring.boot.version}</version>
  </dependency>
  <dependency>
    <groupId>commons-beanutils</groupId>
    <artifactId>commons-beanutils</artifactId>
    <version>${beanutils.version}</version>
  </dependency>
  <dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>${fastjson.version}</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <version>${spring.boot.version}</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>javax.xml.bind</groupId>
    <artifactId>jaxb-api</artifactId>
    <version>2.3.0</version>
  </dependency>
  <dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-impl</artifactId>
    <version>2.3.0</version>
  </dependency>
  <dependency>
    <groupId>com.sun.xml.bind</groupId>
    <artifactId>jaxb-core</artifactId>
    <version>2.3.0</version>
  </dependency>
  <dependency>
    <groupId>javax.activation</groupId>
    <artifactId>activation</artifactId>
    <version>1.1.1</version>
  </dependency>
</dependencies>
```

### 5.4. 数据库配置

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        enable_lazy_load_no_trans: false
  datasource:
    username: root
    password: MySQL@root123456
    url: jdbc:mysql://192.168.9.16:44060/spring_aop_datalog
    driver-class-name: com.mysql.cj.jdbc.Driver
  data:
    mongodb:
      host: 192.168.9.16
      port: 44061
      database: spring_aop_datalog
      repositories:
        enabled: true
```

### 5.5. 领域模型

```java
public class Action {
    private String id;
    private Long objectId;
    private String objectClass;
    private String operator;
    private Date operateTime;
    private ActionType actionType;
    private List<ChangeItem> changes = new ArrayList<>();
    // ...
}
```

```java
public enum ActionType {
    INSERT("添加", 1),
    UPDATE("更新", 2),
    DELETE("删除", 3);
    // ...
}
```

```java
public class ChangeItem {
    private String field;
    private String fieldShowName;
    private String oldValue;
    private String newValue;
    // ...
}
```

```java
public interface ActionDao extends MongoRepository<Action, String> {
}
```

### 5.6. 产品模型

```java
@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    @Datalog(name = "产品名称")
    private String name;
    private String category;
    private String detail;
    private BigDecimal buyPrice;
    private BigDecimal sellPrice;
    private String provider;
    private Date onlineTime;
    private Date updateTime;
    public Product() {
    }
    public Product(String name) {
        this.name = name;
    }
    // ...
}
```

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Datalog {
    // 中文字段名
    String name();
}
```

```java
public interface ProductDao extends JpaRepository<Product, Long> {
    Product findById(Long id);
}
```

### 5.7. 切面设计

```java
@Aspect
@Component
public class DatalogAspect {
    private static final Logger logger = LoggerFactory.getLogger(DatalogAspect.class);
    @Autowired
    ActionDao actionDao;
    @Pointcut("execution(public * org.example.spring_aop.datalog.dao.*.save*(..))")
    public void save() {
    }
    @Pointcut("execution(public * org.example.spring_aop.datalog.dao.*.delete*(..))")
    public void delete() {
    }
    // ...
}
```

### 5.8. Around 注解

```java
/**
 * 1、判断是什么类型的操作,增加 / 删除 / 还是更新
 * 增加 / 更新 save(Product), 通过 id 区分是增加还是更新
 * 删除 delete(id)
 * 2、获取 changeItem
 * (1)新增操作, before 直接获取, after 记录下新增之后的 id
 * (2)更新操作, before 获取操作之前的记录, after 获取操作之后的记录,然后 diff
 * (3)删除操作, before 根据 id 取记录
 * 3、保存操作记录
 *
 * @param pjp pjp
 * @return Object
 * @throws Throwable Throwable
 */
@Around("save() || delete()")
public Object addOperateLog(ProceedingJoinPoint pjp) throws Throwable {
    Object returnObj = null;
    // TODO BEFORE OPERATION init action
    String method = pjp.getSignature().getName();
    ActionType actionType = null;
    Action action = new Action();
    Long id = null;
    Object oldObj = null;
    try {
        if ("save".equals(method)) {
            // insert or update
        } else if ("delete".equals(method)) {
            // delete
        }
        returnObj = pjp.proceed(pjp.getArgs());
        // TODO AFTER OPERATION save action
        action.setActionType(actionType);
        if (ActionType.INSERT == actionType) {
            // new id
        } else if (ActionType.UPDATE == actionType) {
            // old value and new value
        }
        // dynamic get from threadLocal / session
        action.setOperator("admin");
        action.setOperateTime(new Date());
        actionDao.save(action);
    } catch (Exception e) {
        logger.error(e.getMessage(), e);
    }
    return returnObj;
}
```

```java
// insert or update
Object obj = pjp.getArgs()[0];
try {
    id = Long.valueOf(PropertyUtils.getProperty(obj, "id").toString());
} catch (Exception e) {
    // ignore
}
if (id == null) {
    actionType = ActionType.INSERT;
    List<ChangeItem> changeItems = DiffUtil.getInsertChangeItems(obj);
    action.getChanges().addAll(changeItems);
    action.setObjectClass(obj.getClass().getName());
} else {
    actionType = ActionType.UPDATE;
    action.setObjectId(id);
    oldObj = DiffUtil.getObjectById(pjp.getTarget(), id);
    action.setObjectClass(oldObj.getClass().getName());
}
```

```java
// delete
id = Long.valueOf(pjp.getArgs()[0].toString());
actionType = ActionType.DELETE;
oldObj = DiffUtil.getObjectById(pjp.getTarget(), id);
ChangeItem changeItem = DiffUtil.getDeleteChangeItem(oldObj);
action.getChanges().add(changeItem);
action.setObjectId(Long.valueOf(pjp.getArgs()[0].toString()));
action.setObjectClass(oldObj.getClass().getName());
```

```java
// new id
Object newId = PropertyUtils.getProperty(returnObj, "id");
action.setObjectId(Long.valueOf(newId.toString()));
```

```java
// old value and new value
Object newObj = DiffUtil.getObjectById(pjp.getTarget(), id);
List<ChangeItem> changeItems = DiffUtil.getChangeItems(oldObj, newObj);
action.getChanges().addAll(changeItems);
```

### 5.9. 获取新旧值

```java
public static List<ChangeItem> getChangeItems(Object oldObj, Object newObj) {
    Class cl = oldObj.getClass();
    List<ChangeItem> changeItems = new ArrayList<ChangeItem>();
    // 获取字段中文名称
    Map<String, String> fieldCnNameMap = getFieldNameMap(cl);
    try {
        BeanInfo beanInfo = Introspector.getBeanInfo(cl, Object.class);
        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            String field = propertyDescriptor.getName();
            // 获取字段旧值
            String oldProp = getValue(PropertyUtils.getProperty(oldObj, field));
            // 获取字段新值
            String newProp = getValue(PropertyUtils.getProperty(newObj, field));
            // 对比新旧值
            if (!oldProp.equals(newProp)) {
                ChangeItem changeItem = new ChangeItem();
                changeItem.setField(field);
                String cnName = fieldCnNameMap.get(field);
                changeItem.setFieldShowName(StringUtils.isEmpty(cnName) ? field : cnName);
                changeItem.setNewValue(newProp);
                changeItem.setOldValue(oldProp);
                changeItems.add(changeItem);
            }
        }
    } catch (Exception e) {
        logger.error("There is error when convert changeItem", e);
    }
    return changeItems;
}
```

### 5.10. 程序入口

```java
@SpringBootApplication
public class DatalogApplication {
    public static void main(String[] args) {
        SpringApplication.run(DatalogApplication.class, args);
    }
}
```

### 5.11. 单元测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class DatalogApplicationTests {
    @Autowired
    ProductDao productDao;
    // ...
}
```

```java
@Test
public void testInsert() {
    Product product = new Product();
    product.setName("dell computer");
    product.setOnlineTime(new Date());
    product.setBuyPrice(new BigDecimal("29.5"));
    product.setCategory("computer");
    product.setDetail("this is a dell notebook");
    product.setUpdateTime(new Date());
    productDao.save(product);
    System.out.println("new product id:" + product.getId());
}
```

```java
@Test
public void testUpdate() {
    Product product = productDao.findOne(1L);
    product.setName("test-update");
    product.setBuyPrice(new BigDecimal("23.5"));
    product.setOnlineTime(new Date());
    productDao.save(product);
}
```

```java
@Test
public void testDelete() {
    productDao.delete(1L);
}
```

### 5.12. 小结

- 利用反射获取新旧值
- 利用 Around 的 advice 去记录修改记录
- 利用注解去增加中文字段名

## 6. 课程总结

### 6.1. 课程要点

- AOP 的适用范围及优劣势
- AOP 的概念及 Spring 的切面表达式
- AOP 的实现原理及运用

### 6.2. 使用 Spring AOP 的注意事项/坑

- 不要把重要的业务逻辑放到 AOP 中处理
- 无法拦截 `static`、`final` 方法，`private` 方法
- 无法拦截内部方法调用

#### 6.2.1. 第二次调用无法走缓存

```java
@Component
public class MenuService {
    @Cacheable(cacheNames = {"menu"})
    public List<String> getMenuList() {
        System.out.println("mock:get from db");
        return Arrays.asList("article", "comment", "admin");
    }
    public List<String> getRecommends() {
        System.out.println("inner call");
        return getMenuList();
    }
}
```

```java
@Test
public void testInnerCall() {
    System.out.println("call:" + menuService.getRecommends());
    System.out.println("call:" + menuService.getRecommends());
}
```

```java
inner call
mock:get from db
call:[article, comment, admin]
inner call
mock:get from db
call:[article, comment, admin]
```

#### 6.2.2. 第二次调用走缓存

```java
@Component
public class MenuService {
    @Cacheable(cacheNames = {"menu"})
    public List<String> getMenuList() {
        System.out.println("mock:get from db");
        return Arrays.asList("article", "comment", "admin");
    }
    public List<String> getRecommends() {
        System.out.println("inner call");
        MenuService proxy = ApplicationContextHolder.getContext().getBean(MenuService.class);
        return proxy.getMenuList();
    }
}
```

```java
@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static ApplicationContext ctx;
    public static ApplicationContext getContext() {
        return ctx;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        ctx = applicationContext;
    }
}
```

```java
@Test
public void testInnerCall() {
    System.out.println("call:" + menuService.getRecommends());
    System.out.println("call:" + menuService.getRecommends());
}
```

```java
inner call
mock:get from db
call:[article, comment, admin]
inner call
call:[article, comment, admin]
```

### 6.3. 小结

- 合理利用面向切面编程提升代码质量
- 掌握 Spring AOP 概念及实现原理
- 了解 AOP 的优缺点及 Spring AOP 的使用局限
