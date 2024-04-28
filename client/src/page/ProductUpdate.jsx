import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import verifySellerRole from '../utility/verifySellerRole';

function ProductUpdate(){
    const [user, setUser] = useState();
    const [error, setError] = useState();

    const navigate = useNavigate();

    useEffect(() => {
        const userData = JSON.parse(sessionStorage.getItem('USER_JWT'));

        if(!verifySellerRole(userData)){
            navigate('/')
        }

        setUser(userData);

    }, [navigate])

    return(
        <div className="product-update-container">
            <h2>Product update</h2>

        </div>
    )
}

export default ProductUpdate;