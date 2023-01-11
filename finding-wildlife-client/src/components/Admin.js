import { useHistory } from "react-router-dom";

function Admin(){

    const history = useHistory();

    function handleAddOrganism(){
        history.push('/organism/add')
    }
    // function handleUpdateOrganism(){
    //     history.push('/organism/update')
    // }

    // function handleAddParkOrganism(){
    //     history.push('/park-organism/add')
    // }

    // function handleUpdateParkOrganism(){
    //     history.push('/park-organism/update')
    // }

    return(<>
    <div className="admin-container text-center">
    <h1 className="text-center admin-h2">Admin Home Page</h1>
    <button type="button" className="btn btn-success" onClick={handleAddOrganism}>Add Wildlife</button>
    {/* <button type="button" className="btn btn-success" onClick={handleUpdateOrganism}>Update Wildlife</button>
    <button type="button" className="btn btn-success" onClick={handleAddParkOrganism}>Add Wildlife to Park</button>
    <button type="button" className="btn btn-success" onClick={handleUpdateParkOrganism}>Update Wildlife in Park</button> */}
    </div>

    </>);
}

export default Admin;