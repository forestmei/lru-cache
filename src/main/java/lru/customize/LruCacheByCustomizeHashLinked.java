package lru.customize;

import lru.Storage;

/**
 * 自定义实现
 * 自定义哈希表、链表
 * 容积固定，哈希表不需要扩容
 *
 * @author <a href="mailto:meilinsenchn@gmail.com">meils</a>
 * @date 2021/9/24 1:04 下午
 * @since
 */
public class LruCacheByCustomizeHashLinked<K, V> extends LruCache<K, V>{

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K, V>[] table;
    private Node<K, V> head;
    private Node<K, V> tail;
    private int size;

    public LruCacheByCustomizeHashLinked(int capacity, Storage lowSpeedStorage){
        super(capacity, lowSpeedStorage);
        table = new Node[(int) (capacity/DEFAULT_LOAD_FACTOR)];
    }

    /**
     * next是重复哈希的单向链表
     * before和after是数据访问顺序的双向链表，最近使用的在前
     * @param <K>
     * @param <V>
     */
    class Node<K, V>{
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        Node<K,V> before;
        Node<K, V> after;

        Node(int hash, K key, V value){
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public V get(K key) {
        int hash = hash(key);
        Node<K,V> node = getNode(hash, key);
        if(node == null){
            V value = lowSpeedStorage.get(key);
            addFirstNode(hash, key, value);
            return value;
        }else {
            moveNodeToFirst(node);
            return node.value;
        }
    }

    private void moveNodeToFirst(Node<K,V> node){
        if(node == head){
            return;
        }
        Node<K, V> before = node.before;
        Node<K, V> after = node.after;
        before.after = after;
        if(after != null){
            after.before = before;
            tail = after;
        }else {
            tail = before;
        }
        node.before = null;
        node.after = head;
        head.before = node;
        head = node;
    }

    private void addFirstNode(int hash, K key, V value){
        if(size == capacity){
            removeLastNode();
        }
        Node<K, V> newNode = new Node<>(hash, key, value);
        if(head == null){
            head = tail = newNode;
        }else {
            newNode.after = head;
            head.before = newNode;
            head = newNode;
        }
        int index = index(hash);
        Node<K, V> tabNode = table[index];
        if(tabNode == null){
            table[index] = newNode;
        }else {
            Node<K, V> curNode = tabNode;
            while (curNode.next != null){
                curNode = curNode.next;
            }
            curNode.next = newNode;
        }
        size++;
    }

    private void removeLastNode(){
        //移除双向链表中的节点
        Node<K, V> removeNode = tail;
        Node<K, V> preNode = removeNode.before;
        preNode.after = null;
        removeNode.before = null;
        tail = preNode;

        //移除哈希表中的节点
        int index = index(removeNode.hash);
        Node<K, V> firstNode = table[index];
        if(firstNode.key.equals(removeNode.key)){
            table[index] = null;
        }else {
            Node<K, V> curNode = firstNode;
            while (curNode.next != null){
                if(curNode.next.key.equals(removeNode.key)){
                    break;
                }
                curNode = curNode.next;
            }
            Node<K, V> delNode = curNode.next;
            curNode.next = delNode.next;
            delNode.next = null;
        }
        size--;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    final Node<K,V> getNode(int hash, Object key) {
        Node<K, V> node = table[index(hash)];
        if(node == null){
            return null;
        }
        Node<K, V> curNode = node;
        while (curNode != null){
            if(curNode.key.equals(key)){
                return curNode;
            }
            curNode = curNode.next;
        }
        return null;
    }

    private int index(int hash){
        return (table.length - 1) & hash;
    }

    /**
     * 没有实现低速存储，这里加一个put方法用于添加元素以便测试
     * @param key
     * @param value
     */
    public void put(K key, V value){
        addFirstNode(hash(key), key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<K, V> curNode = head;
        while (curNode != null){
            sb.append('[');
            sb.append(curNode.key);
            sb.append(',');
            sb.append(curNode.value);
            sb.append(']');
            sb.append(" -> ");
            curNode = curNode.after;
        }
        sb.append("null");
        return sb.toString();
    }
}
