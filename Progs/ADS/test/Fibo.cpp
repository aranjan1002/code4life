#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Fibo.h"


FiboHeap::FiboHeap()
{
        m_pRootHeap = NULL;
        m_pMinHeap = NULL;
}

FiboHeap::~FiboHeap()
{
        stHeapNode* pNode = m_pRootHeap;
        
        while (pNode != NULL)
        {
                stHeapNode* pNextNode = pNode->pRight;

                m_fnDeleteTree(pNode);
                pNode = pNextNode;
        }
}

void FiboHeap::Insert(stHeapInfo hpInfo)
{
        stHeapNode* pNewNode = m_fnNewNode(hpInfo);

        m_fnAddToRootHeap(pNewNode);
}

void FiboHeap::ExtractMin(stHeapInfo* hpInfo)
{
        if (m_pMinHeap == NULL) return;

        *hpInfo = m_pMinHeap->hpInfo;

        // Move the children into root
        m_fnLevelUp(m_pMinHeap);

        // Remove the minimum node
        m_fnRemoveNode(m_pMinHeap);
        m_pMinHeap = NULL;

        // Re-assign a new one for m_pMinHeap
        m_fnAssignMinNode();

        // Consolidate the heap trees
        m_fnConsolidate();
}

void FiboHeap::DumpHeap()
{
        stHeapNode* pNode = m_pRootHeap;
        
        while (pNode != NULL)
        {
                printf("------------------------\n");
                if (pNode == m_pMinHeap)
                {
                        printf("*");
                }
                m_fnDumpTree(pNode);
                pNode = pNode->pRight;
        }
}

void FiboHeap::m_fnAssignMinNode()
{
        stHeapNode* pCheckNode = m_pRootHeap;

        while (pCheckNode != NULL)
        {
                if (m_pMinHeap == NULL || m_pMinHeap->hpInfo.nKey > pCheckNode->hpInfo.nKey)
                {
                        m_pMinHeap = pCheckNode;
                }
                
                pCheckNode = pCheckNode->pRight;
        }
}

void FiboHeap::m_fnRemoveNode(stHeapNode* pNode)
{
        if (pNode == NULL) return;

        if (pNode == m_pRootHeap)
        {
                m_pRootHeap = m_pRootHeap->pRight;
        }
        if (pNode->pLeft)  pNode->pLeft->pRight = pNode->pRight;
        if (pNode->pRight) pNode->pRight->pLeft = pNode->pLeft;
        delete pNode;
}

void FiboHeap::m_fnLevelUp(stHeapNode* pNode)
{
        for (int i = 0; i < pNode->nChild; i++)
        {
                stHeapNode* pChildNode = pNode->ppChild[i];

                m_fnAddToRootHeap(pChildNode);
                pChildNode->pParent = NULL;
                pNode->ppChild[i] = NULL;
        }

        pNode->nChild = 0;
}

void FiboHeap::m_fnAddToRootHeap(stHeapNode* pNode)
{
        // First time
        if (m_pRootHeap == NULL)
        {
                m_pRootHeap = pNode;
                m_pMinHeap = pNode;
                return;
        }

        // Add to the most left of the root heap
        pNode->pRight = m_pRootHeap;
        m_pRootHeap->pLeft = pNode;
        m_pRootHeap = pNode;

        // Check the minimum heap
        if (m_pMinHeap->hpInfo.nKey > pNode->hpInfo.nKey)
        {
                m_pMinHeap = pNode;
        }
}

stHeapNode* FiboHeap::m_fnNewNode(stHeapInfo hpInfo)
{
        stHeapNode* pNode = new stHeapNode;

        pNode->pLeft    = NULL;
        pNode->pRight   = NULL;
        pNode->pParent  = NULL;
        pNode->hpInfo   = hpInfo;
        pNode->nDegree  = 0;
        pNode->nChild   = 0;
        
        memset(pNode->ppChild, 0, sizeof(pNode->ppChild));

        return pNode;
}

void FiboHeap::m_fnConsolidate()
{
        // Make sure the degree of each tree is unique
        stHeapNode* ppDegreeNode[MAX_DEGREES];
        stHeapNode* pNode = m_pRootHeap;

        memset(ppDegreeNode, 0, sizeof(ppDegreeNode));
        while (pNode != NULL)
        {
                if (ppDegreeNode[pNode->nDegree] == NULL)
                {
                        ppDegreeNode[pNode->nDegree] = pNode;
                        pNode = pNode->pRight;
                }
                else    // merge the two trees
                {
                        stHeapNode* pPreNode = ppDegreeNode[pNode->nDegree];
                        
                        if (pPreNode->hpInfo.nKey > pNode->hpInfo.nKey)
                        {
                                m_fnAttachTree(pPreNode, pNode);
                        }
                        else
                        {
                                m_fnAttachTree(pNode, pPreNode);
                        }

                        // Reset the search
                        memset(ppDegreeNode, 0, sizeof(ppDegreeNode));                  
                        pNode = m_pRootHeap;
                }
        }
}

void FiboHeap::m_fnAttachTree(stHeapNode* pFromNode, stHeapNode* pToNode)
{
        if (pFromNode == m_pRootHeap)
        {
                m_pRootHeap = m_pRootHeap->pRight;
        }
        
        // Break the link of pFromNode
        if (pFromNode->pLeft)  pFromNode->pLeft->pRight = pFromNode->pRight;
        if (pFromNode->pRight) pFromNode->pRight->pLeft = pFromNode->pLeft;
        pFromNode->pLeft = NULL;
        pFromNode->pRight = NULL;

        // Move the pFromNode under the pToNode
        pToNode->ppChild[ pToNode->nChild ] = pFromNode;
        pFromNode->pParent = pToNode;
        pToNode->nChild++;
        pToNode->nDegree++;
}

void FiboHeap::m_fnDumpTree(stHeapNode* pNode)
{	 int i;
        printf("(%d)\n", pNode->hpInfo.nKey);
        for (i = 0; i < pNode->nChild; i++)
        {
                stHeapNode* pChildNode = pNode->ppChild[i];

                printf("  %d", pChildNode->hpInfo.nKey);
        }
        printf("\n");

        for (i = 0; i < pNode->nChild; i++)
        {
                stHeapNode* pChildNode = pNode->ppChild[i];
                m_fnDumpTree(pChildNode);
        }
}

void FiboHeap::m_fnDeleteTree(stHeapNode* pNode)
{
        if (pNode == NULL) return;
        
        for (int i = 0; i < pNode->nChild; i++)
        {
                stHeapNode* pChildNode = pNode->ppChild[i];
                m_fnDeleteTree(pChildNode);
        }

        delete pNode;
}