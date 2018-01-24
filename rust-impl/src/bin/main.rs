#![feature(iterator_step_by)]

fn main() {
    assert_eq!(generate_input(10, 26), generate_input_with_strategy(10, 26, power));
    let vec = generate_input_with_strategy(17, 18, next).into_iter().skip_while(|&item| item < 231424).collect::<Vec<usize>>();
    println!("generate_input_with_strategy(17, 18) = {:?}", (vec));
}


fn power(num: u32) -> Vec<usize> {
    let r = 2usize.pow(num);
    (r..r+1).into_iter().collect::<Vec<usize>>()
}

fn next(num: u32) -> Vec<usize> {
    let r = 2usize.pow(num);
    let l = 2usize.pow(num+1);
    (r..).step_by(1024).take_while(|i| i <= &l).collect::<Vec<usize>>()
}

fn generate_input(min_size: u32, max_size: u32) -> Vec<usize> {
    (min_size..max_size).map(|size| 2usize.pow(size))
        .collect::<Vec<usize>>()
}

fn generate_input_with_strategy(min_size: u32, max_size: u32, strategy: fn(u32) -> Vec<usize>) -> Vec<usize> {
    (min_size..max_size)
        .flat_map(|size| strategy(size).into_iter())
        .collect::<Vec<usize>>()
}
