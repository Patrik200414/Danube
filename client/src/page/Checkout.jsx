import useVerifyUserAccess from "../utility/customHook/useVerifyUserAccess";

function Checkout(){
    useVerifyUserAccess('/verification/user/password', '/login');

    return(
        <div className="checkout-dashboard">
            <h1>Checkout!</h1>
        </div>
    )
}

export default Checkout;