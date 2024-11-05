console.log("hello from script");


// var animal = "cat";//ES5

//let,const

let animal = 'cat';
let animal2;
animal2 = 'cat';
const animal3 = 'cat3';

//object - reference value
const MY_OBJECT = {

    key: "value",
    key2: 2,
    key3: true,
    name:"amy",
    age:18

}

console.log("My_OBJECT name is ",MY_OBJECT.name);

MY_OBJECT.name = "jackie";

console.log("My_OBJECT name is ",MY_OBJECT.name);

const MY_ARRAY=["a",1,true,MY_OBJECT];

console.log("MY_ARRAY[0] is", MY_ARRAY[0]);
//never use "==" in JS
console.log("1=== true", 1 === true);

//function declaration

function intro(name) {

    console.log("name is", name);
    


}
intro("Yang");
//function expression

const sayMyName = function (name) {
    console.log("name is", name);
}

sayMyName("wanf");

// arrow func () => {}

const addTwoNumbers = (num1, num2) => {

    console.log("sum is", num1 +num2);

}
addTwoNumbers(1,2);

//one line expression
const addThreeNumbers = (num1,num2,num3) => num1+num2+num3;

console.log(addThreeNumbers(1,2,3));


// Immediately Invoked Function Expression

(()=>{
    // do something
    console.log("IIFE");
}) ()

//后面 “（）” 相当于调用定义的func 定义完立马调用
// call back funtion
//if  line 83 got crush -> the dog won't be able to make sound 
//to decouple here we use the call back func
function animalMakeSound(animal){

    if(animal === "cat"){
        console.log("Meu");
    } else if(animal === "dog"){
        console.log("wang");
    }

}

// now using call back func
function animalMakeSoundV2(soundFunction){
    soundFunction();
}
const catSound =() => {
    console.log("meow");
};
const dogSound =() => {
    console.log("wang");
};

animalMakeSoundV2(catSound);
animalMakeSoundV2(dogSound);