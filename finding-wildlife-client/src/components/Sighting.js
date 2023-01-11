import { useContext } from "react";
import { useHistory } from "react-router-dom";
import UserContext from "../UserContext";

function Sighting({sighting, handleDelete}) {
    const history = useHistory();
    const authManager = useContext(UserContext);

    function confirmDelete(){
        if(window.confirm("Are you sure you want to this sighting!")){
            handleDeleteSighting();
        }
    }

    const handleDeleteSighting = () => {
        const init = {
            method: 'DELETE',
            headers: {
                Authorization: `Bearer ${authManager.user.token}`
            }
        };

        fetch(`http://localhost:8080/finding-wildlife/sighting/${sighting.sightingId}/${authManager.user.appUserId}`, init)
        .then(response => {
            switch(response.status) {
                case 204:
                    return null;
                case 404:
                    history.push('./not-found')
                    break;
                default:
                    return Promise.reject('Something Went Wrong');
            }
        })
        .then(response => {
            if(!response) {
                handleDelete(sighting.sightingId)
            }
            else{
                console.log(response)
            }
        })
        .catch(error => history.push('./error', {errorMessage: error}))
      }
    
    const handleEditSighting = () => {
        history.push(`/sightings/update/${sighting.sightingId}`);
      }
    
    return (
        <tr>
            <td>{sighting.date}</td>
            <td>{sighting.time}</td>
            <td>{sighting.comments}</td>
            <td>{sighting.park.parkName}</td>
            <td>{sighting.organism.commonName}</td>
            <td>
                {authManager.user && <button type="button" className="btn btn-primary mr-3 mb-2" onClick={handleEditSighting}>Edit</button>}
                {authManager.user && <button type="button" className="btn btn-danger" onClick={confirmDelete}>Delete</button>}
            </td>
        </tr>
    )  
}

export default Sighting;