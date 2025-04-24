import { Button, Divider } from "@mantine/core";
import Profile from "../Components/TalentProfile/Profile";
import { IconArrowLeft } from "@tabler/icons-react";
import { Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { getAllProfiles } from "../Services/ProfileService";
import { useDispatch } from "react-redux";
import { hideOverlay, showOverlay } from "../Slices/OverlaySlice";

const TalentProfilePage = () => {
    const navigate=useNavigate();
    const dispatch=useDispatch();
    const [talents, setTalents] = useState<any>([]);
    useEffect(() => {
        dispatch(showOverlay());
        getAllProfiles().then((res) => {
            setTalents(res);
        }).catch((err) => console.log(err))
        .finally(()=>dispatch(hideOverlay()));
    },[])
    return (
        <div className="min-h-[90vh] bg-white font-['poppins'] p-4  ">
            <Divider size="xs" mx="md" />
                <Button my="sm" onClick={()=>navigate(-1)} color="oceanTeal.4" leftSection={<IconArrowLeft size={20} />} variant="light">Back</Button>
            <div className="flex gap-5 lg-mx:flex-wrap">
                <Profile />
            </div>
        </div>
    )
}
export default TalentProfilePage;