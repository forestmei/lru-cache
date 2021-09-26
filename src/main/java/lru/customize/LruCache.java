package lru.customize;

import lru.Storage;

/**
 * TODO
 *
 * @author <a href="mailto:meilinsenchn@gmail.com">meils</a>
 * @date 2021/9/24 12:34 下午
 * @since
 */
public abstract class LruCache<K, V> implements Storage<K, V> {

    protected final int capacity;
    //低速存储，所有数据可以从这里去到，未实现
    protected final Storage<K, V> lowSpeedStorage;

    public LruCache(int capacity, Storage lowSpeedStorage) {
        this.capacity = capacity;
        this.lowSpeedStorage = lowSpeedStorage;
    }
}
