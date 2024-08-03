import { useState } from "react";
import ProductContainer from "../component/product/ProductContainer";
import { useFetch } from "../utility/customHook/fetchHooks";

function ProductsPage(){
    const itemPerPage = 9;
    const [pageNumber, setPageNumber] = useState(0);
    const [pageProducts, , error] = useFetch(`/api/product?pageNumber=${pageNumber}&itemPerPage=${itemPerPage}`);
    console.log(pageProducts);


    return(
        <div className="home">
            {error && <p className="error-message">{error}</p>}
            <ProductContainer pageProducts={pageProducts} onPaginationNumberChange={(pageNumber) => setPageNumber(pageNumber)}/>
        </div>
    )
}

export default ProductsPage;