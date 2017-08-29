#![feature(alloc)]
#![feature(plugin)]
#![feature(const_fn)]
#![feature(test)]
#![feature(shared)]

#![cfg_attr(feature="clippy", feature(plugin))]

#![cfg_attr(feature="clippy", plugin(clippy))]

pub mod queues;
