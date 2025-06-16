import { useEffect, useState } from "react";
import Sort from "../FindJobs/Sort";
import TalentCard from "./TalentCard";
import { getAllProfiles } from "../../Services/ProfileService";
import { useDispatch, useSelector } from "react-redux";
import { resetFilter } from "../../Slices/FilterSlice";
import { hideOverlay, showOverlay } from "../../Slices/OverlaySlice";
import { notifications } from "@mantine/notifications";

const Talents = () => {
    const dispatch = useDispatch();
    const [talents, setTalents] = useState<any[]>([]);
    const filter = useSelector((state: any) => state.filter);
    const sort = useSelector((state: any) => state.sort);
    const [filteredTalents, setFilteredTalents] = useState<any[]>([]);

    useEffect(() => {
        dispatch(resetFilter());
        dispatch(showOverlay());
        getAllProfiles()
            .then((res) => {
                // Filter profiles to only include those with accountType "APPLICANT"
                const applicantProfiles = Array.isArray(res)
                    ? res.filter((profile: any) => profile.accountType === "APPLICANT")
                    : [];
                setTalents(applicantProfiles);
            })
            .catch((err) => {
                console.log(err);
                // Optionally, show a notification similar to AdminDashboard
                notifications.show({
                    title: "Error",
                    message: err.response?.data?.message || "Failed to load profiles",
                    color: "red",
                });
                setTalents([]);
            })
            .finally(() => dispatch(hideOverlay()));
    }, [dispatch]);

    useEffect(() => {
        if (sort === "Experience: Low to High") {
            setTalents([...talents].sort((a: any, b: any) => a.totalExp - b.totalExp));
        } else if (sort === "Experience: High to Low") {
            setTalents([...talents].sort((a: any, b: any) => b.totalExp - a.totalExp));
        }
    }, [sort, talents]);

    useEffect(() => {
        let filtered = talents;

        if (filter.name) {
            filtered = filtered.filter((talent: any) =>
                talent.name.toLowerCase().includes(filter.name.toLowerCase())
            );
        }
        if (filter["Job Title"] && filter["Job Title"].length > 0) {
            filtered = filtered.filter((talent: any) =>
                filter["Job Title"]?.some((x: any) =>
                    talent.jobTitle?.toLowerCase().includes(x.toLowerCase())
                )
            );
        }
        if (filter.Location && filter.Location.length > 0) {
            filtered = filtered.filter((talent: any) =>
                filter.Location?.some((x: any) =>
                    talent.location?.toLowerCase().includes(x.toLowerCase())
                )
            );
        }
        if (filter.Skills && filter.Skills.length > 0) {
            filtered = filtered.filter((talent: any) =>
                filter.Skills?.some((x: any) =>
                    talent.skills?.some((y: any) => y.toLowerCase().includes(x.toLowerCase()))
                )
            );
        }
        if (filter.exp && filter.exp.length > 0) {
            filtered = filtered.filter(
                (talent: any) => filter.exp[0] <= talent.totalExp && talent.totalExp <= filter.exp[1]
            );
        }
        setFilteredTalents(filtered);
    }, [filter, talents]);

    return (
        <div className="px-5 py-5">
            <div className="flex justify-between mt-5">
                <div className="text-2xl font-semibold">Talents</div>
                <Sort />
            </div>
            <div className="flex mt-10 flex-wrap gap-5 justify-between">
                {filteredTalents.length === 0 ? (
                    <div className="text-center text-xl text-deepSlate-300">
                        No Talents Found
                    </div>
                ) : (
                    filteredTalents.map((talent: any, index: any) => (
                        <TalentCard key={index} {...talent} />
                    ))
                )}
            </div>
        </div>
    );
};

export default Talents;