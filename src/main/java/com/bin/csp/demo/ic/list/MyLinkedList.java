package com.bin.csp.demo.ic.list;

public class MyLinkedList {
    int size;
    ListNode head;

    public MyLinkedList(){
        size = 0;
        head = new ListNode(0);
    }

    public int get(int index){
        if(index < 0 || index >= size){
            return -1;
        }

        ListNode cur = head;
        while(index-- > 0){
            cur = cur.next;
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

        ListNode cur = head;
        while(index-- >0){
            cur = cur.next;
        }

        ListNode toAdd = new ListNode(val);
        toAdd.next = cur.next;
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
        ListNode cur = head;
        while(index-- > 0){
            cur = cur.next;
        }
        cur.next = cur.next.next;

    }

}
