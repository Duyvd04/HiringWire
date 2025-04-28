import { Link, useLocation } from "react-router-dom";
import { useSelector } from "react-redux";

const NavLinks = () => {
    const user = useSelector((state: any) => state.user || {});
    const location = useLocation();

    const links = [
        {
            name: "Find Jobs",
            url: "find-jobs",
            roles: ["APPLICANT"],
        },
        {
            name: "Post Job",
            url: "post-job/0",
            roles: ["EMPLOYER"],
        },
        {
            name: "Posted Jobs",
            url: "posted-jobs/0",
            roles: ["EMPLOYER"],
        },
        {
            name: "Job History",
            url: "job-history",
            roles: ["APPLICANT"],
        },
        {
            name: "Admin Dashboard",
            url: "admin-dashboard",
            roles: ["ADMIN"],
        },
    ];

    const filteredLinks = user?.accountType
        ? links.filter((link) => link.roles.includes(user.accountType))
        : [];

    return (
        <div className="flex bs-mx:!hidden gap-5 text-deepSlate-900 h-full items-center">
            {filteredLinks.map((link) => (
                <div
                    key={link.url}
                    className={`${
                        location.pathname === "/" + link.url
                            ? "border-oceanTeal-500 text-oceanTeal-500"
                            : "border-transparent"
                    } border-t-[3px] h-full flex items-center`}
                >
                    <Link className="hover:text-oceanTeal-600" to={link.url}>
                        {link.name}
                    </Link>
                </div>
            ))}
        </div>
    );
};

export default NavLinks;