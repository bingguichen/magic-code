package com.bin.csp.demo.ic.list;

public class Intersection {
    public static void MyPrint(ListNode t1) // 输出链表
    {
        while (t1 != null) {
            System.out.println(t1.val);
            t1 = t1.next;
        }
    }

    //这里可写相关操作函数

    public static void main(String[] args) throws InterruptedException {
        ListNode t8 = new ListNode(2);
        ListNode t7 = new ListNode(4, t8);
        ListNode t6 = new ListNode(5, t7);
        ListNode t5 = new ListNode(6, t6);
        ListNode t4 = new ListNode(7, t5);
        ListNode t3 = new ListNode(8, t4);
        ListNode t2 = new ListNode(9, t3);
        ListNode t1 = new ListNode(3, t2);

        ListNode t9 = new ListNode(4);
        ListNode t10 = new ListNode(4, t9);
        ListNode t11 = new ListNode(5, t10);
        ListNode t12 = new ListNode(11, t11);

        System.out.println("--------链表t1-------");
        MyPrint(t1);
        System.out.println("--------链表t12-------");
        MyPrint(t12);
        System.out.println("--------链表t1&&t12-------");
        MyPrint(getIntersectionNode(t1,t12));


    }

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode curA = headA;
        ListNode curB = headB;
        int lenA = 0, lenB = 0;
        while (curA != null){
            lenA++;
            curA = curA.next;
        }
        while (curB != null){
            lenB++;
            curB = curB.next;
        }
        curA = headA;
        curB = headB;

        if(lenB > lenA){
            int lenTmp = lenA;
            lenA = lenB;
            lenB = lenTmp;
            ListNode nodeTmp = curA;
            curA = curB;
            curB = nodeTmp;
        }

        int gap = lenA - lenB;
        while (gap-- > 0){
            curA= curA.next;
        }
        while (curA != null){
            if(curA.val == curB.val){
                return curA;
            }
            curA = curA.next;
            curB = curB.next;
        }
        return null;
    }

}
