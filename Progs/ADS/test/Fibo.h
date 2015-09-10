#ifndef _H_FIBONACCI_HEAP_H_
#define MAX_DEGREES             20

//////////////////////////////////////////////////////////////////////////
typedef struct  
{
        int nKey;
        
} stHeapInfo;

typedef struct stHeapNode
{
        stHeapNode* pParent;
        stHeapNode* pLeft;
        stHeapNode* pRight;     
        stHeapNode* ppChild[MAX_DEGREES];
        int                     nChild;

        int                     nDegree;
        stHeapInfo      hpInfo;
} stHeapNode;

class FiboHeap
{
public:
        FiboHeap();
        ~FiboHeap();
        
        void Insert(stHeapInfo hpInfo);
        void ExtractMin(stHeapInfo* hpInfo);

        void DumpHeap();

private:
        stHeapNode* m_fnNewNode(stHeapInfo hpInfo);
        void m_fnAddToRootHeap(stHeapNode* pNode);
        void m_fnConsolidate();
        void m_fnAttachTree(stHeapNode* pFromNode, stHeapNode* pToNode);
        void m_fnLevelUp(stHeapNode* pNode);            // Move the children of pNode into root heap
        void m_fnRemoveNode(stHeapNode* pNode);
        void m_fnAssignMinNode();                                       // find a minimum node
        void m_fnDumpTree(stHeapNode* pNode);
        void m_fnDeleteTree(stHeapNode* pNode);

        stHeapNode* m_pRootHeap;
        stHeapNode* m_pMinHeap;
};


#endif /* _H_FIBONACCI_HEAP_H_ */