package lru.primitive;

import lru.Storage;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于原生LinkedHashMap的lru缓存策略实现
 * 需要实现 {@link LinkedHashMap#removeEldestEntry}方法
 *
 * @author <a href="mailto:meilinsenchn@gmail.com">meils</a>
 * @date 2021/9/24 12:34 下午
 * @since
 */
public class LruCacheByPrimitiveHashLinked<K, V> extends LinkedHashMap<K, V> implements Storage<K, V> {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    protected final int capacity;
    //低速存储，所有数据可以从这里去到，未实现
    protected final Storage<K, V> lowSpeedStorage;

    public LruCacheByPrimitiveHashLinked(int capacity, Storage lowSpeedStorage) {
        //要调用LinkedHashMap的此构造方法，以访问顺序排序
        super(capacity, DEFAULT_LOAD_FACTOR, true);
        this.capacity = capacity;
        this.lowSpeedStorage = lowSpeedStorage;
    }

    //需要实现自定义删除策略
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    @Override
    public V get(Object key){
        V value = super.get(key);
        if(value == null){
            value = lowSpeedStorage.get((K)key);
            super.put((K)key, value);
        }
        return value;
    }
}
