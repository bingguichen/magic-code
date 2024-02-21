package com.bin.csp.demo.ic.list;


public class Reverse {

    public static void MyPrint(ListNode t1) // 输出链表
    {
        while (t1 != null) {
            System.out.println(t1.val);
            t1 = t1.next;
        }
    }

    //这里可写相关操作函数

    public static void main(String[] args) {
        ListNode t1 = new ListNode(1);
        ListNode t2 = new ListNode(2);
        ListNode t3 = new ListNode(3);
        ListNode t4 = new ListNode(4);
        ListNode t5 = new ListNode(5);

        t1.next = t2;
        t2.next = t3;
        t3.next = t4;
        t4.next = t5;
        t5.next = null;
        System.out.println("--------链表t1-------");
        MyPrint(t1);

        System.out.println("--------链表t1 after-------");
        MyPrint(reverseList(t1));
    }

    public static ListNode reverseList01(ListNode head){
        ListNode pre = null;
        ListNode cur = head;
        ListNode tmp = null;
        while (cur != null){
            tmp = cur.next;
            cur.next = pre;
            pre = cur;
            cur = tmp;
        }
        return pre;
    }
    public static ListNode reverseList(ListNode head){
        return reverse(null, head);
    }
    public static ListNode reverse(ListNode pre, ListNode cur){
        if(cur == null) return pre;
        ListNode tmp = null;
        tmp = cur.next;
        cur.next = pre;
        return reverse(cur, tmp);
    }
}
