package frank.interview;

import java.util.HashMap;
import java.util.Map;

class Node<K, V> {
	private Node<K, V> previous;
	private Node<K, V> next;
	private K key;
	private V value;
	Node() {
		previous = null;
		next = null;
		key = null;
		value = null;
	}
	Node(K key, V value) {
		previous = null;
		next = null;
		this.key = key;
		this.value = value;
	}
	
	void setPrevious(Node<K, V> previous) {
		this.previous = previous;
	}
	Node<K, V> getPrevious() {
		return this.previous;
	}
	
	void setNext(Node<K, V> next) {
		this.next = next;
	}
	Node<K, V> getNext() {
		return this.next;
	}
	
	void setKey(K key) {
		this.key = key;
	}
	K getKey() {
		return this.key;
	}
	
	void setValue(V value) {
		this.value = value;
	}
	V getValue() {
		return this.value;
	}
}

class EasyList<K, V> {
	private Node<K, V> head = null;
	private Node<K, V> tail = null;
	
	void pushBack(Node<K, V> node) {
		if (head == null) {
			head = node;
			tail = head;
		} else {
			node.setPrevious(tail);
			tail.setNext(node);
			tail = node;
		}
	}
	
	Node<K, V> popFront() {
		if (head == null || tail == null) {
			return null;
		}
		Node<K, V> oldHead = head;
		head = head.getNext();
		head.setPrevious(null);
		oldHead.setNext(null);
		return oldHead;
	}
	
	Node<K, V> popBack() {
		if (head == null || tail == null) {
			return null;
		}
		Node<K, V> oldTail = tail;
		tail = tail.getPrevious();
		tail.setNext(null);
		oldTail.setPrevious(null);
		return oldTail;
	}
	
	void remove(Node<K, V> node) {
		if (node == null) {
			return;
		}
		if (node.getPrevious() == null) {
			// node is head
			if (node != head) {
				System.out.println("node should be head!");
				return;
			}
			popFront();
		} else if (node.getNext() == null) {
			// node is tail
			if (node != tail) {
				System.out.println("node should be tail!");
				return;
			}
			popBack();
		} else {
			node.getPrevious().setNext(node.getNext());
			node.getNext().setPrevious(node.getPrevious());
			node.setNext(null);
			node.setPrevious(null);
		}
	}
	
	long size() {
		long count = 0;
		Node<K, V> node = head;
		while(node != null) {
			node = node.getNext();
			count++;
		}
		return count;
	}
	
	void print() {
		Node<K, V> node = head;
		while(node != null) {
			System.out.println(node.getKey()+": \t"+node.getValue());
			node = node.getNext();
		}
		System.out.println("-------------------------------------------");
	}
}

public class CacheLRU<K, V> implements Cache<K, V> {

	private Map<K, Node<K, V>> data;
	private EasyList<K, V> queue;
	long cacheSize = 4;
	long keyCount = 0;
	
	public CacheLRU(long cacheSize) {
		this.cacheSize = cacheSize;
		this.data = new HashMap<K, Node<K, V>>();
		this.queue = new EasyList<K, V>();
	}
	
	@Override
	public void put(K key, V value) {
		Node<K, V> node = data.get(key);
		if (node == null) {
			// this key is not in cache yet
			node = new Node<K, V>(key, value);
			if (keyCount >= cacheSize) {
				Node<K, V> oldHead = queue.popFront();
				data.remove(oldHead.getKey());
				keyCount--;
			}
			queue.pushBack(node);
			data.put(key, node);
			keyCount++;
		} else {
			// key is already in cache
			queue.remove(node);
			queue.pushBack(node);
			node.setValue(value);
		}
	}

	@Override
	public V get(K key) {
		Node<K, V> node = data.get(key);
		if (node == null) {
			// not such key
			return null;
		} else {
			queue.remove(node);
			queue.pushBack(node);
			return node.getValue();
		}
	}

	@Override
	public void dump() {
		long countInHash = data.size();
		long countInQueue = queue.size();
		if ((keyCount == countInHash) && (keyCount == countInQueue)) {
			queue.print();
		} else {
			System.out.println("Inconsistency in cache: keyCount="+keyCount
					+", countInHash="+countInHash+", countInQueue"+countInQueue);
		}
	}

}
