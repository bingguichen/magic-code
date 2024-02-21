package com.bin.csp.demo.ic.list;

public class Cycle {
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
        ListNode t6 = new ListNode(6);
        ListNode t7 = new ListNode(7);
        ListNode t8 = new ListNode(8);
        ListNode t9 = new ListNode(9);
        ListNode t10 = new ListNode(10);
        ListNode t11 = new ListNode(11);
        ListNode t12 = new ListNode(12);

        t1.next = t2;
        t2.next = t3;
        t3.next = t4;
        t4.next = t5;
        t5.next = t6;
        t6.next = t7;
        t7.next = t8;
        t8.next = t9;
        t9.next = t10;
        t10.next = t11;
        t11.next = t12;
        t12.next = t6;
        System.out.println("--------链表t1-------");
        MyPrint(t1);

        System.out.println("--------链表t1 after-------");
        MyPrint(detectCycle(t1));
    }

    public static ListNode detectCycle(ListNode head){
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null){
            slow = slow.next;
            fast = fast.next.next;
            if(slow == fast){
                ListNode index1 = fast;
                ListNode index2 = head;
                while (index1 != index2){
                    index1 = index1.next;
                    index2 = index2.next;
                }
                return index1;
            }
        }
        return null;
    }
}
