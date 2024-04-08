import NavBar from "../component/NavBar";
import ProductContainer from "../component/ProductContainer";
import { useEffect, useState } from "react";

function Home(){
    const [products, setProducts] = useState();
    const [itemPerPage, setItemPerPage] = useState(9);
    const [pageNumber, setPageNumber] = useState(0);

    const [error, setError] = useState();

    useEffect(() => {
        const getProducts = async () => {
            const productsData = await fetch(`/api/product?pageNumber=${pageNumber}&itemPerPage=${itemPerPage}`);
            const productResponse = await productsData.json();
            console.log(productResponse);
            if(productsData.ok){
                setProducts(productResponse);
                setError();
            } else{
                setError(productResponse.errorMessage);
            }
        }

        getProducts();
    }, [itemPerPage, pageNumber])


    return(
        <div className="home">
            <NavBar />
            {error && <p className="error-message">{error}</p>}
            <ProductContainer products={products}/>
        </div>
    )
}

export default Home;