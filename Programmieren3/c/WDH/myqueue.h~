#ifndef MYQUEUE_H
#define MYQUEUE_H

struct queue{
  struct queue *next;
  int value;
};

typedef struct queue *lqueue;

extern lqueue enter(lqueue, int);
extern int is_empty(lqueue);
extern int front(lqueue);
extern lqueue leave(lqueue);

#endif
