package com.bin.csp.demo.ic.list;

public class Remove {
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
        MyPrint(removeNthFromEnd(t1, 2));
    }

    /**
     * 添加虚节点方式
     * 时间复杂度 O(n)
     * 空间复杂度 O(1)
     * @param head
     * @param target
     * @return
     */
    public static ListNode removeElements01(ListNode head, int target) {
        if (head == null) {
            return head;
        }
        // 因为删除可能涉及到头节点，所以设置dummy节点，统一操作
        ListNode dummy = new ListNode(-1, head);
        ListNode pre = dummy;
        ListNode cur = head;
        while (cur != null) {
            if (cur.val == target) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return dummy.next;
    }

    /**
     * 不添加虚拟节点方式
     * 时间复杂度 O(n)
     * 空间复杂度 O(1)
     * @param head
     * @param val
     * @return
     */
    public static ListNode removeElements02(ListNode head, int val) {
        while (head != null && head.val == val) {
            head = head.next;
        }
        // 已经为null，提前退出
        if (head == null) {
            return head;
        }
        // 已确定当前head.val != val
        ListNode pre = head;
        ListNode cur = head.next;
        while (cur != null) {
            if (cur.val == val) {
                pre.next = cur.next;
            } else {
                pre = cur;
            }
            cur = cur.next;
        }
        return head;
    }

    /**
     * 不添加虚拟节点and pre Node方式
     * 时间复杂度 O(n)
     * 空间复杂度 O(1)
     * @param head
     * @param val
     * @return
     */
    public static ListNode removeElements03(ListNode head, int val) {
        while(head!=null && head.val==val){
            head = head.next;
        }
        ListNode curr = head;
        while(curr!=null){
            while(curr.next!=null && curr.next.val == val){
                curr.next = curr.next.next;
            }
            curr = curr.next;
        }
        return head;
    }

    public static ListNode removeNthFromEnd(ListNode head, int n){
        ListNode dummyNode = new ListNode(0);
        dummyNode.next = head;
        ListNode fastIndex = dummyNode;
        ListNode slowIndex = dummyNode;

        while(n-- > 0 && fastIndex != null){
            fastIndex = fastIndex.next;
        }
//        for(int i = 0; i <= n; i++){
//            fastIndex = fastIndex.next;
//        }

        while (fastIndex != null){
            fastIndex = fastIndex.next;
            slowIndex = slowIndex.next;
        }

        slowIndex.next = slowIndex.next.next;
        return dummyNode.next;
    }
}
