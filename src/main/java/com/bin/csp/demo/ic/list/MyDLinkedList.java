package com.bin.csp.demo.ic.list;

public class MyDLinkedList {
    int size;
    DListNode head, tail;

    public MyDLinkedList(){
        this.size = 0;
        this.head = new DListNode(0);
        this.tail = new DListNode(0);
        head.next = tail;
        tail.pre = head;
    }

    public int get(int index){
        if(index < 0 || index >= size){
            return -1;
        }

        DListNode cur = this.head;
        if(index >= size / 2){
            cur = this.tail;
            while (index-- > 0){
                cur = cur.pre;
            }
        }else {
            while (index-- > 0) {
                cur = cur.next;
            }
        }
        return cur.val;
    }

    // 在第 index 个节点之前插入一个新节点，例如index为0，那么新插入的节点为链表的新头节点。
    // 如果 index 等于链表的长度，则说明是新插入的节点为链表的尾结点
    // 如果 index 大于链表的长度，则返回空
    public void addAtIndex(int index, int val){
        if(index > size) return;
        if(index < 0) index = 0;
        size++;

        DListNode cur = head;
        while(index-- >0){
            cur = cur.next;
        }

        DListNode toAdd = new DListNode(val);
        toAdd.next = cur.next;
        cur.next.pre = toAdd;
        toAdd.pre = cur;
        cur.next = toAdd;
    }

    //在链表最前面插入一个节点，等价于在第0个元素前添加
    public void addAtHead(int val){
        addAtIndex(0, val);
    }

    //在链表的最后插入一个节点，等价于在(末尾+1)个元素前添加
    public void addAtTail(int val){
        addAtIndex(size, val);
    }

    public void deleteAtIndex(int index){
        if(index < 0 || index >= size) return;
        size--;
        if(index ==0){
            head = head.next;
            return;
        }
        DListNode cur = head;
        while(index-- > 0){
            cur = cur.next;
        }
        cur.next.next.pre = cur;
        cur.next = cur.next.next;

    }

}
