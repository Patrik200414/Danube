function buttonObjectGenerator(actionName, buttonText, isDynamic, linkTo, onClick){
    return {
        actionName, buttonText, isDynamic, linkTo, onClick
    }
}

export default buttonObjectGenerator;