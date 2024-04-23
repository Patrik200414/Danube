import { useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";

function Product(){
    const {id} = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        if(isNaN(id)){
            navigate('/')
        }
    }, [id, navigate])

    return(
        <div>
            <h1>Product: {id}</h1>
        </div>
    )
}

export default Product;