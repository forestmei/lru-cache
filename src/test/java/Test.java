import lru.customize.LruCacheByCustomizeHashLinked;

/**
 * TODO
 *
 * @author <a href="mailto:meilinsenchn@gmail.com">meils</a>
 * @date 2021/9/26 9:50 上午
 * @since
 */
public class Test {

    public static void main(String[] args) {
        LruCacheByCustomizeHashLinked<String, String> lruCacha = new LruCacheByCustomizeHashLinked<>(3, null);
        lruCacha.put("1", "a");
        lruCacha.put("2", "b");
        lruCacha.put("3", "c");
        System.out.println(lruCacha);
        System.out.println(lruCacha.get("1"));
        System.out.println(lruCacha);
        lruCacha.put("4", "d");
        System.out.println(lruCacha);
    }
}
