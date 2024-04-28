function verifySellerRole(userData){
    if(!userData){
        return false;
    }

    if(!userData.roles.includes('ROLE_SELLER')){
        return false;
    }

    return true;
}

export default verifySellerRole;