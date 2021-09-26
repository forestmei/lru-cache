package lru;

/**
 * TODO
 *
 * @author <a href="mailto:meilinsenchn@gmail.com">meils</a>
 * @date 2021/9/24 12:33 下午
 * @since
 */
public interface Storage<K, V> {

    V get(K key);
}
