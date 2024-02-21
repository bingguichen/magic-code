package com.bin.csp.demo.ic.list;

public class Swap {
    public static void MyPrint(ListNode t1) // 输出链表
    {
        while (t1 != null) {
            System.out.println(t1.val);
            t1 = t1.next;
        }
    }

    //这里可写相关操作函数

    public static void main(String[] args) throws InterruptedException {
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

        System.out.println("--------链表t1 after 01 swap-------");
        MyPrint(swapPairs01(t1));
        System.out.println("--------链表t1 after 01 swap back-------");
        MyPrint(swapPairs01(t2));
        System.out.println("--------链表t1 after 02 swap-------");
        MyPrint(swapPairs02(t1));
        System.out.println("--------链表t1 after 02 swap back-------");
        MyPrint(swapPairs02(t2));

    }

    public static ListNode swapPairs01(ListNode head){
        ListNode dummyHead = new ListNode(-1); // 设置一个虚拟头结点
        dummyHead.next = head; // 将虚拟头结点指向head，这样方便后面做删除操作
        ListNode cur = dummyHead;
        ListNode firstnode, secondnode, temp3;
        while (cur.next != null && cur.next.next != null){
            temp3 = cur.next.next.next;
            firstnode = cur.next;
            secondnode = cur.next.next;
            cur.next = secondnode;       // 步骤一
            secondnode.next = firstnode; // 步骤二
            firstnode.next = temp3;      // 步骤三
            cur = firstnode; // cur移动，准备下一轮交换
        }
        return dummyHead.next;
    }

    public static ListNode swapPairs02(ListNode head){
        if(head == null || head.next == null) return head;
        ListNode next = head.next;
        ListNode newNode = swapPairs02(next.next);
        next.next = head;
        head.next = newNode;
        return next;
    }
}
