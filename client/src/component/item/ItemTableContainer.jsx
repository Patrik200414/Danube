import ItemTable from "./ItemTable";
import Proptypes from "prop-types";

function ItemTableContainer({tableName, renderElement}){
    return(
        <div className="item-table-container">
            <h3>{tableName}: </h3>
            <ItemTable renderElement={renderElement}/>
        </div>
    )
}

ItemTableContainer.propTypes = {
    tableName: Proptypes.string,
    renderElement: Proptypes.func
}

export default ItemTableContainer;