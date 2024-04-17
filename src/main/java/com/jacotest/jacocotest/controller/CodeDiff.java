package com.jacotest.jacocotest.controller;


import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class CodeDiff {

    private String oldPath;

    private String newPath;

    public CodeDiff(String oldPath, String newPath) {
        this.oldPath = oldPath;
        this.newPath = newPath;
    }

    private void getCodeDiff() {

        File oldDirPath = new File(this.oldPath);
        File newDirPath = new File(this.newPath);


        try (
                Repository repository = new RepositoryBuilder().setWorkTree(oldDirPath).build()) {
            // 准备两个树解析器，表示两个源代码目录
            CanonicalTreeParser oldTreeParser = prepareTreeParser(repository, Constants.HEAD);
            CanonicalTreeParser newTreeParser = prepareTreeParser(newDirPath);

            // 比较两个源代码目录的差异
            try (DiffFormatter diffFormatter = new DiffFormatter(System.out)) {
                diffFormatter.setRepository(repository);
                diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
                diffFormatter.setDetectRenames(true);
                List<DiffEntry> diffEntries = diffFormatter.scan(oldTreeParser, newTreeParser);

                // 输出差异项
                System.out.println("Changes:");
                for (DiffEntry diffEntry : diffEntries) {
                    System.out.println(diffEntry.getChangeType() + " " + diffEntry.getNewPath());
                }
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }
    // 两个应用版本的源代码目录路径


    private static CanonicalTreeParser prepareTreeParser(Repository repository, String ref) throws IOException {
        try (RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit(repository.resolve(ref));
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, commit.getTree());
            }
            return treeParser;
        }
    }

    private static CanonicalTreeParser prepareTreeParser(File directory) throws IOException {
        try (Repository repository = new RepositoryBuilder().setWorkTree(directory).build()) {
            try (RevWalk walk = new RevWalk(repository)) {
                RevCommit commit = walk.parseCommit(repository.resolve(Constants.HEAD));
                try (ObjectReader reader = repository.newObjectReader()) {
                    CanonicalTreeParser treeParser = new CanonicalTreeParser();
                    treeParser.reset(reader, commit.getTree().getId());
                    return treeParser;
                }
            }
        }
    }

}


